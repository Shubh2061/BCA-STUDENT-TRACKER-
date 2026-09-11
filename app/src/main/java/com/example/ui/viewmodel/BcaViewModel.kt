package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BcaRepository
import com.example.data.model.*
import com.example.data.prepopulate.BcaPresetData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BcaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BcaRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BcaRepository(db.bcaDao())
    }

    // Profile & Semester State
    val profile = repository.profileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _selectedSemester = MutableStateFlow(1)
    val selectedSemester: StateFlow<Int> = _selectedSemester.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profileFlow.collect { prof ->
                if (prof != null) {
                    _selectedSemester.value = prof.currentSemester
                }
            }
        }
    }

    // Dynamic queries based on selected semester
    @OptIn(ExperimentalCoroutinesApi::class)
    val subjects: StateFlow<List<BcaSubject>> = _selectedSemester.flatMapLatest { sem ->
        repository.getSubjectsForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val syllabusTopics: StateFlow<List<SyllabusTopic>> = _selectedSemester.flatMapLatest { sem ->
        repository.getSyllabusForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val timetableClasses: StateFlow<List<TimetableClass>> = _selectedSemester.flatMapLatest { sem ->
        repository.getTimetableForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val labExperiments: StateFlow<List<LabExperiment>> = repository.getLabExperiments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCodingExercises: StateFlow<List<CodingExercise>> = repository.getAllCodingExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val assignments: StateFlow<List<Assignment>> = _selectedSemester.flatMapLatest { sem ->
        repository.getAssignmentsForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val exams: StateFlow<List<ExamEntry>> = _selectedSemester.flatMapLatest { sem ->
        repository.getExamsForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<QuickNote>> = _selectedSemester.flatMapLatest { sem ->
        repository.getNotesForSemester(sem)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Overall metrics calculation
    val overallAttendancePct: StateFlow<Double> = subjects.map { list ->
        if (list.isEmpty()) 100.0
        else {
            val totalPresent = list.sumOf { it.presentClasses }
            val totalClasses = list.sumOf { it.totalClasses }
            if (totalClasses == 0) 100.0 else (totalPresent.toDouble() / totalClasses) * 100.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 100.0)

    val overallSyllabusPct: StateFlow<Double> = syllabusTopics.map { list ->
        if (list.isEmpty()) 0.0
        else {
            val completed = list.count { it.status == "COMPLETED" }
            val inProgress = list.count { it.status == "IN_PROGRESS" }
            ((completed + inProgress * 0.5) / list.size.toDouble()) * 100.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val assignmentsCompletionPct: StateFlow<Double> = assignments.map { list ->
        if (list.isEmpty()) 0.0
        else (list.count { it.isCompleted }.toDouble() / list.size) * 100.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val labsCompletionPct: StateFlow<Double> = labExperiments.map { list ->
        if (list.isEmpty()) 0.0
        else (list.count { it.isCompleted }.toDouble() / list.size) * 100.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Current Day & Next Class computation
    data class NextClassInfo(
        val classItem: TimetableClass?,
        val startsInMinutes: Long,
        val isOngoing: Boolean,
        val statusText: String
    )

    fun getTodayClasses(allClasses: List<TimetableClass>): List<TimetableClass> {
        val calendar = Calendar.getInstance()
        // Calendar: SUNDAY = 1, MONDAY = 2, ..., SATURDAY = 7
        // Our dayOfWeek: 1 = Mon, 2 = Tue, ..., 6 = Sat, 7 = Sun
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            else -> 1
        }
        return allClasses.filter { it.dayOfWeek == dayOfWeek }.sortedBy { it.startTime }
    }

    fun getNextClassInfo(todayClasses: List<TimetableClass>): NextClassInfo {
        if (todayClasses.isEmpty()) {
            return NextClassInfo(null, -1, false, "No classes scheduled for today")
        }

        val calendar = Calendar.getInstance()
        val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

        for (cls in todayClasses) {
            val startParts = cls.startTime.split(":").mapNotNull { it.toIntOrNull() }
            val endParts = cls.endTime.split(":").mapNotNull { it.toIntOrNull() }

            if (startParts.size == 2 && endParts.size == 2) {
                val startMin = startParts[0] * 60 + startParts[1]
                val endMin = endParts[0] * 60 + endParts[1]

                if (currentMinutes in startMin..endMin) {
                    val remaining = endMin - currentMinutes
                    return NextClassInfo(cls, remaining.toLong(), true, "Happening Now (ends in ${remaining}m)")
                } else if (startMin > currentMinutes) {
                    val diff = startMin - currentMinutes
                    val status = if (diff < 60) "Starts in $diff minutes" else "Starts in ${diff / 60}h ${diff % 60}m"
                    return NextClassInfo(cls, diff.toLong(), false, status)
                }
            }
        }

        // All classes for today have ended
        return NextClassInfo(null, -1, false, "All classes finished for today 🎉")
    }

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..21 -> "Good Evening"
            else -> "Hello"
        }
    }

    fun getTodayFormatted(): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    // Actions
    fun switchSemester(semester: Int) {
        _selectedSemester.value = semester
        viewModelScope.launch {
            val currentProf = profile.value
            if (currentProf != null && currentProf.currentSemester != semester) {
                repository.saveProfile(currentProf.copy(currentSemester = semester))
            }
        }
    }

    fun saveProfile(profile: StudentProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _selectedSemester.value = profile.currentSemester
        }
    }

    fun setupWithPresets(profile: StudentProfile) {
        viewModelScope.launch {
            repository.seedDefaultData(profile)
            _selectedSemester.value = profile.currentSemester
        }
    }

    fun markAttendance(subject: BcaSubject, isPresent: Boolean) {
        viewModelScope.launch {
            repository.recordAttendance(subject, isPresent)
        }
    }

    fun updateSubjectAttendance(subjectId: Long, present: Int, absent: Int) {
        viewModelScope.launch {
            repository.updateAttendanceCounts(subjectId, present, absent)
        }
    }

    fun addSubject(subject: BcaSubject) {
        viewModelScope.launch {
            repository.addSubject(subject)
        }
    }

    fun updateSubject(subject: BcaSubject) {
        viewModelScope.launch {
            repository.updateSubject(subject)
        }
    }

    fun deleteSubject(subject: BcaSubject) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    fun updateTopicStatus(topicId: Long, status: String) {
        viewModelScope.launch {
            repository.updateTopicStatus(topicId, status)
        }
    }

    fun addTopic(topic: SyllabusTopic) {
        viewModelScope.launch {
            repository.addTopic(topic)
        }
    }

    fun deleteTopic(topic: SyllabusTopic) {
        viewModelScope.launch {
            repository.deleteTopic(topic)
        }
    }

    fun addTimetableClass(entry: TimetableClass) {
        viewModelScope.launch {
            repository.addTimetableClass(entry)
        }
    }

    fun updateTimetableClass(entry: TimetableClass) {
        viewModelScope.launch {
            repository.updateTimetableClass(entry)
        }
    }

    fun deleteTimetableClass(entry: TimetableClass) {
        viewModelScope.launch {
            repository.deleteTimetableClass(entry)
        }
    }

    fun toggleCodingExercise(exercise: CodingExercise) {
        viewModelScope.launch {
            repository.updateExercise(exercise.copy(isCompleted = !exercise.isCompleted))
        }
    }

    fun addCodingExercise(exercise: CodingExercise) {
        viewModelScope.launch {
            repository.addExercise(exercise)
        }
    }

    fun deleteCodingExercise(exercise: CodingExercise) {
        viewModelScope.launch {
            repository.deleteExercise(exercise)
        }
    }

    fun toggleLabExperiment(experiment: LabExperiment) {
        viewModelScope.launch {
            repository.updateLabExperiment(experiment.copy(isCompleted = !experiment.isCompleted))
        }
    }

    fun updateLabExperiment(experiment: LabExperiment) {
        viewModelScope.launch {
            repository.updateLabExperiment(experiment)
        }
    }

    fun addLabExperiment(experiment: LabExperiment) {
        viewModelScope.launch {
            repository.addLabExperiment(experiment)
        }
    }

    fun deleteLabExperiment(experiment: LabExperiment) {
        viewModelScope.launch {
            repository.deleteLabExperiment(experiment)
        }
    }

    fun toggleAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment.copy(isCompleted = !assignment.isCompleted))
        }
    }

    fun addAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.addAssignment(assignment)
        }
    }

    fun deleteAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
        }
    }

    fun addExam(exam: ExamEntry) {
        viewModelScope.launch {
            repository.addExam(exam)
        }
    }

    fun updateExam(exam: ExamEntry) {
        viewModelScope.launch {
            repository.updateExam(exam)
        }
    }

    fun deleteExam(exam: ExamEntry) {
        viewModelScope.launch {
            repository.deleteExam(exam)
        }
    }

    fun addNote(note: QuickNote) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun updateNote(note: QuickNote) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: QuickNote) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun seedSemesterIfEmpty(sem: Int) {
        viewModelScope.launch {
            val existing = repository.getSubjectsForSemester(sem).first()
            if (existing.isEmpty()) {
                val subjects = BcaPresetData.getSuggestedSubjectsForSemester(sem)
                repository.daoSubjectInsertBatch(subjects)
            }
        }
    }

    private suspend fun BcaRepository.daoSubjectInsertBatch(subs: List<BcaSubject>) {
        subs.forEach { addSubject(it) }
    }
}
