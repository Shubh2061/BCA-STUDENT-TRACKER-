package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BcaDao {

    // Profile
    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<StudentProfile?>

    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): StudentProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StudentProfile)

    // Subjects
    @Query("SELECT * FROM bca_subjects WHERE semester = :semester ORDER BY name ASC")
    fun getSubjectsForSemester(semester: Int): Flow<List<BcaSubject>>

    @Query("SELECT * FROM bca_subjects ORDER BY semester ASC, name ASC")
    fun getAllSubjects(): Flow<List<BcaSubject>>

    @Query("SELECT * FROM bca_subjects WHERE id = :id LIMIT 1")
    fun getSubjectById(id: Long): Flow<BcaSubject?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: BcaSubject): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<BcaSubject>): List<Long>

    @Update
    suspend fun updateSubject(subject: BcaSubject)

    @Delete
    suspend fun deleteSubject(subject: BcaSubject)

    @Query("UPDATE bca_subjects SET presentClasses = :present, absentClasses = :absent WHERE id = :subjectId")
    suspend fun updateAttendance(subjectId: Long, present: Int, absent: Int)

    // Syllabus Topics
    @Query("SELECT * FROM syllabus_topics WHERE subjectId = :subjectId ORDER BY unitNumber ASC, sortOrder ASC, id ASC")
    fun getTopicsForSubject(subjectId: Long): Flow<List<SyllabusTopic>>

    @Query("SELECT * FROM syllabus_topics WHERE semester = :semester ORDER BY subjectId ASC, unitNumber ASC")
    fun getTopicsForSemester(semester: Int): Flow<List<SyllabusTopic>>

    @Query("SELECT * FROM syllabus_topics ORDER BY semester ASC, subjectId ASC")
    fun getAllTopics(): Flow<List<SyllabusTopic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: SyllabusTopic): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<SyllabusTopic>)

    @Query("UPDATE syllabus_topics SET status = :status WHERE id = :topicId")
    suspend fun updateTopicStatus(topicId: Long, status: String)

    @Delete
    suspend fun deleteTopic(topic: SyllabusTopic)

    // Timetable
    @Query("SELECT * FROM timetable_classes WHERE semester = :semester ORDER BY dayOfWeek ASC, startTime ASC")
    fun getTimetableForSemester(semester: Int): Flow<List<TimetableClass>>

    @Query("SELECT * FROM timetable_classes WHERE semester = :semester AND dayOfWeek = :dayOfWeek ORDER BY startTime ASC")
    fun getTimetableForDay(semester: Int, dayOfWeek: Int): Flow<List<TimetableClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableClass(entry: TimetableClass): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableClasses(entries: List<TimetableClass>)

    @Update
    suspend fun updateTimetableClass(entry: TimetableClass)

    @Delete
    suspend fun deleteTimetableClass(entry: TimetableClass)

    // Coding Exercises
    @Query("SELECT * FROM coding_exercises WHERE subjectId = :subjectId ORDER BY id ASC")
    fun getExercisesForSubject(subjectId: Long): Flow<List<CodingExercise>>

    @Query("SELECT * FROM coding_exercises ORDER BY subjectId ASC, id ASC")
    fun getAllExercises(): Flow<List<CodingExercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: CodingExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<CodingExercise>)

    @Update
    suspend fun updateExercise(exercise: CodingExercise)

    @Delete
    suspend fun deleteExercise(exercise: CodingExercise)

    // Lab Experiments
    @Query("SELECT * FROM lab_experiments ORDER BY subjectId ASC, experimentNumber ASC")
    fun getAllLabExperiments(): Flow<List<LabExperiment>>

    @Query("SELECT * FROM lab_experiments WHERE subjectId = :subjectId ORDER BY experimentNumber ASC")
    fun getLabExperimentsForSubject(subjectId: Long): Flow<List<LabExperiment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabExperiment(experiment: LabExperiment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabExperiments(experiments: List<LabExperiment>)

    @Update
    suspend fun updateLabExperiment(experiment: LabExperiment)

    @Delete
    suspend fun deleteLabExperiment(experiment: LabExperiment)

    // Assignments
    @Query("SELECT * FROM assignments WHERE semester = :semester ORDER BY isCompleted ASC, dueDate ASC")
    fun getAssignmentsForSemester(semester: Int): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments ORDER BY isCompleted ASC, dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment): Long

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    // Exams
    @Query("SELECT * FROM exams WHERE semester = :semester ORDER BY date ASC, time ASC")
    fun getExamsForSemester(semester: Int): Flow<List<ExamEntry>>

    @Query("SELECT * FROM exams ORDER BY date ASC, time ASC")
    fun getAllExams(): Flow<List<ExamEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntry): Long

    @Update
    suspend fun updateExam(exam: ExamEntry)

    @Delete
    suspend fun deleteExam(exam: ExamEntry)

    // Quick Notes
    @Query("SELECT * FROM quick_notes WHERE semester = :semester ORDER BY updatedAt DESC")
    fun getNotesForSemester(semester: Int): Flow<List<QuickNote>>

    @Query("SELECT * FROM quick_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<QuickNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: QuickNote): Long

    @Update
    suspend fun updateNote(note: QuickNote)

    @Delete
    suspend fun deleteNote(note: QuickNote)

    // Attendance Records
    @Query("SELECT * FROM attendance_records WHERE subjectId = :subjectId ORDER BY timestamp DESC")
    fun getRecordsForSubject(subjectId: Long): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceRecord): Long
}
