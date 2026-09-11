package com.example.data.db

import com.example.data.model.*
import com.example.data.prepopulate.BcaPresetData
import kotlinx.coroutines.flow.Flow

class BcaRepository(private val dao: BcaDao) {

    val profileFlow: Flow<StudentProfile?> = dao.getProfileFlow()

    suspend fun getProfile(): StudentProfile? = dao.getProfile()

    suspend fun saveProfile(profile: StudentProfile) = dao.insertOrUpdateProfile(profile)

    fun getSubjectsForSemester(semester: Int): Flow<List<BcaSubject>> =
        dao.getSubjectsForSemester(semester)

    fun getAllSubjects(): Flow<List<BcaSubject>> = dao.getAllSubjects()

    suspend fun addSubject(subject: BcaSubject): Long = dao.insertSubject(subject)

    suspend fun updateSubject(subject: BcaSubject) = dao.updateSubject(subject)

    suspend fun deleteSubject(subject: BcaSubject) = dao.deleteSubject(subject)

    suspend fun markAttendance(subjectId: Long, isPresent: Boolean) {
        val allSubjects = dao.getSubjectsForSemester(1) // or current
        // We can query specific subject or do direct calculation
    }

    suspend fun updateAttendanceCounts(subjectId: Long, present: Int, absent: Int) {
        dao.updateAttendance(subjectId, present, absent)
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        dao.insertAttendanceRecord(
            AttendanceRecord(
                subjectId = subjectId,
                date = today,
                status = if (present > 0) "RECORDED" else "RECORDED"
            )
        )
    }

    suspend fun recordAttendance(subject: BcaSubject, isPresent: Boolean) {
        val newPresent = if (isPresent) subject.presentClasses + 1 else subject.presentClasses
        val newAbsent = if (!isPresent) subject.absentClasses + 1 else subject.absentClasses
        dao.updateAttendance(subject.id, newPresent, newAbsent)
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        dao.insertAttendanceRecord(
            AttendanceRecord(
                subjectId = subject.id,
                date = today,
                status = if (isPresent) "PRESENT" else "ABSENT"
            )
        )
    }

    fun getSyllabusForSubject(subjectId: Long): Flow<List<SyllabusTopic>> =
        dao.getTopicsForSubject(subjectId)

    fun getSyllabusForSemester(semester: Int): Flow<List<SyllabusTopic>> =
        dao.getTopicsForSemester(semester)

    fun getAllSyllabusTopics(): Flow<List<SyllabusTopic>> = dao.getAllTopics()

    suspend fun addTopic(topic: SyllabusTopic) = dao.insertTopic(topic)

    suspend fun updateTopicStatus(topicId: Long, status: String) =
        dao.updateTopicStatus(topicId, status)

    suspend fun deleteTopic(topic: SyllabusTopic) = dao.deleteTopic(topic)

    fun getTimetableForSemester(semester: Int): Flow<List<TimetableClass>> =
        dao.getTimetableForSemester(semester)

    suspend fun addTimetableClass(entry: TimetableClass) = dao.insertTimetableClass(entry)

    suspend fun updateTimetableClass(entry: TimetableClass) = dao.updateTimetableClass(entry)

    suspend fun deleteTimetableClass(entry: TimetableClass) = dao.deleteTimetableClass(entry)

    fun getExercisesForSubject(subjectId: Long): Flow<List<CodingExercise>> =
        dao.getExercisesForSubject(subjectId)

    fun getAllCodingExercises(): Flow<List<CodingExercise>> = dao.getAllExercises()

    suspend fun addExercise(exercise: CodingExercise) = dao.insertExercise(exercise)

    suspend fun updateExercise(exercise: CodingExercise) = dao.updateExercise(exercise)

    suspend fun deleteExercise(exercise: CodingExercise) = dao.deleteExercise(exercise)

    fun getLabExperiments(): Flow<List<LabExperiment>> = dao.getAllLabExperiments()

    suspend fun addLabExperiment(experiment: LabExperiment) = dao.insertLabExperiment(experiment)

    suspend fun updateLabExperiment(experiment: LabExperiment) =
        dao.updateLabExperiment(experiment)

    suspend fun deleteLabExperiment(experiment: LabExperiment) =
        dao.deleteLabExperiment(experiment)

    fun getAssignmentsForSemester(semester: Int): Flow<List<Assignment>> =
        dao.getAssignmentsForSemester(semester)

    fun getAllAssignments(): Flow<List<Assignment>> = dao.getAllAssignments()

    suspend fun addAssignment(assignment: Assignment) = dao.insertAssignment(assignment)

    suspend fun updateAssignment(assignment: Assignment) = dao.updateAssignment(assignment)

    suspend fun deleteAssignment(assignment: Assignment) = dao.deleteAssignment(assignment)

    fun getExamsForSemester(semester: Int): Flow<List<ExamEntry>> =
        dao.getExamsForSemester(semester)

    fun getAllExams(): Flow<List<ExamEntry>> = dao.getAllExams()

    suspend fun addExam(exam: ExamEntry) = dao.insertExam(exam)

    suspend fun updateExam(exam: ExamEntry) = dao.updateExam(exam)

    suspend fun deleteExam(exam: ExamEntry) = dao.deleteExam(exam)

    fun getNotesForSemester(semester: Int): Flow<List<QuickNote>> =
        dao.getNotesForSemester(semester)

    suspend fun addNote(note: QuickNote) = dao.insertNote(note)

    suspend fun updateNote(note: QuickNote) = dao.updateNote(note)

    suspend fun deleteNote(note: QuickNote) = dao.deleteNote(note)

    suspend fun seedDefaultData(profile: StudentProfile) {
        dao.insertOrUpdateProfile(profile)
        val defaultSubjects = BcaPresetData.getSuggestedSubjectsForSemester(profile.currentSemester)
        val insertedIds = dao.insertSubjects(defaultSubjects)
        
        val cSubjectId = insertedIds.getOrNull(0) ?: 1L
        val mathSubjectId = insertedIds.getOrNull(1) ?: 2L
        val compSubjectId = insertedIds.getOrNull(2) ?: 3L
        val labSubjectId = insertedIds.getOrNull(5) ?: insertedIds.lastOrNull() ?: 6L

        // Syllabus
        val topics = BcaPresetData.getSampleSyllabusTopics(cSubjectId, mathSubjectId, compSubjectId)
        dao.insertTopics(topics)

        // Coding exercises
        val codingExercises = BcaPresetData.getSampleCodingExercises(cSubjectId)
        dao.insertExercises(codingExercises)

        // Lab experiments
        val labs = BcaPresetData.getSampleLabExperiments(labSubjectId)
        dao.insertLabExperiments(labs)

        // Timetable
        val sampleSubjectsWithIds = defaultSubjects.mapIndexed { idx, sub ->
            sub.copy(id = insertedIds.getOrElse(idx) { (idx + 1).toLong() })
        }
        val timetable = BcaPresetData.getSampleTimetable(sampleSubjectsWithIds)
        dao.insertTimetableClasses(timetable)

        // Assignments
        val assignments = BcaPresetData.getSampleAssignments(cSubjectId, mathSubjectId)
        assignments.forEach { dao.insertAssignment(it) }

        // Exams
        val exams = BcaPresetData.getSampleExams(cSubjectId, mathSubjectId, compSubjectId)
        exams.forEach { dao.insertExam(it) }

        // Notes
        val notes = BcaPresetData.getSampleNotes(cSubjectId, mathSubjectId)
        notes.forEach { dao.insertNote(it) }
    }
}
