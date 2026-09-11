package com.example.data.prepopulate

import com.example.data.model.*

object BcaPresetData {

    fun getDefaultProfile(): StudentProfile {
        return StudentProfile(
            id = 1,
            name = "Rohan Sharma",
            college = "BCA Institute of Technology",
            year = "1st Year",
            currentSemester = 1,
            section = "A",
            requiredAttendancePercentage = 75.0,
            notificationsEnabled = true,
            reminderMinutesBefore = 15,
            isSetupCompleted = true
        )
    }

    fun getSuggestedSubjectsForSemester(semester: Int): List<BcaSubject> {
        return when (semester) {
            1 -> listOf(
                BcaSubject(
                    semester = 1,
                    name = "Programming in C",
                    code = "BCA-101",
                    teacher = "Prof. Amit Sharma",
                    room = "Room 204",
                    isPractical = false,
                    isProgramming = true,
                    credits = 4,
                    examDate = "2026-10-18",
                    notes = "Core programming subject covering procedural programming and logic building.",
                    colorHex = 0xFF2563EB,
                    presentClasses = 28,
                    absentClasses = 6
                ),
                BcaSubject(
                    semester = 1,
                    name = "Discrete Mathematics",
                    code = "BCA-102",
                    teacher = "Dr. Rekha Verma",
                    room = "Room 205",
                    isPractical = false,
                    isProgramming = false,
                    credits = 4,
                    examDate = "2026-10-21",
                    notes = "Sets, relations, graphs, combinatorics and logic proofs.",
                    colorHex = 0xFF7C3AED,
                    presentClasses = 19,
                    absentClasses = 9
                ),
                BcaSubject(
                    semester = 1,
                    name = "Computer Fundamentals",
                    code = "BCA-103",
                    teacher = "Prof. S. Gupta",
                    room = "Room 201",
                    isPractical = false,
                    isProgramming = false,
                    credits = 3,
                    examDate = "2026-10-24",
                    notes = "Hardware architecture, CPU, memory, OS basics and I/O devices.",
                    colorHex = 0xFF059669,
                    presentClasses = 24,
                    absentClasses = 3
                ),
                BcaSubject(
                    semester = 1,
                    name = "Communication Skills in English",
                    code = "BCA-104",
                    teacher = "Ms. Pooja Mehta",
                    room = "Room 108",
                    isPractical = false,
                    isProgramming = false,
                    credits = 2,
                    examDate = "2026-10-27",
                    notes = "Technical writing, presentations, phonetics and business correspondence.",
                    colorHex = 0xFFD97706,
                    presentClasses = 18,
                    absentClasses = 2
                ),
                BcaSubject(
                    semester = 1,
                    name = "Digital Electronics",
                    code = "BCA-105",
                    teacher = "Dr. K. Rao",
                    room = "Room 202",
                    isPractical = false,
                    isProgramming = false,
                    credits = 3,
                    examDate = "2026-10-30",
                    notes = "Logic gates, Boolean algebra, flip-flops and counters.",
                    colorHex = 0xFFDC2626,
                    presentClasses = 21,
                    absentClasses = 5
                ),
                BcaSubject(
                    semester = 1,
                    name = "C Programming Lab",
                    code = "BCA-106",
                    teacher = "Prof. Amit Sharma",
                    room = "Computer Lab 2",
                    isPractical = true,
                    isProgramming = true,
                    credits = 2,
                    examDate = "2026-11-04",
                    notes = "Hands-on lab programs in C, debugging and viva examination.",
                    colorHex = 0xFF0891B2,
                    presentClasses = 14,
                    absentClasses = 2
                )
            )
            2 -> listOf(
                BcaSubject(semester = 2, name = "Data Structures using C", code = "BCA-201", teacher = "Dr. V. Kapoor", room = "Room 301", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 2, name = "Object Oriented Programming (C++)", code = "BCA-202", teacher = "Prof. R. Mishra", room = "Room 302", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 2, name = "Operating Systems", code = "BCA-203", teacher = "Dr. N. Singh", room = "Room 303", isPractical = false, isProgramming = false, credits = 3),
                BcaSubject(semester = 2, name = "Environmental Science", code = "BCA-204", teacher = "Ms. T. Sen", room = "Room 102", isPractical = false, isProgramming = false, credits = 2),
                BcaSubject(semester = 2, name = "Data Structures Lab", code = "BCA-205", teacher = "Dr. V. Kapoor", room = "Computer Lab 1", isPractical = true, isProgramming = true, credits = 2)
            )
            3 -> listOf(
                BcaSubject(semester = 3, name = "Core Java Programming", code = "BCA-301", teacher = "Prof. M. Joshi", room = "Room 401", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 3, name = "Database Management Systems", code = "BCA-302", teacher = "Dr. S. Nair", room = "Room 402", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 3, name = "Computer Networks", code = "BCA-303", teacher = "Prof. D. Roy", room = "Room 403", isPractical = false, isProgramming = false, credits = 3),
                BcaSubject(semester = 3, name = "Web Technologies (HTML/CSS/JS)", code = "BCA-304", teacher = "Ms. K. Patel", room = "Lab 3", isPractical = false, isProgramming = true, credits = 3),
                BcaSubject(semester = 3, name = "Java & DBMS Lab", code = "BCA-305", teacher = "Prof. M. Joshi", room = "Computer Lab 3", isPractical = true, isProgramming = true, credits = 2)
            )
            4 -> listOf(
                BcaSubject(semester = 4, name = "Python Programming", code = "BCA-401", teacher = "Dr. A. Sen", room = "Room 404", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 4, name = "Software Engineering", code = "BCA-402", teacher = "Prof. P. Das", room = "Room 405", isPractical = false, isProgramming = false, credits = 4),
                BcaSubject(semester = 4, name = "PHP & MySQL Web Dev", code = "BCA-403", teacher = "Ms. H. Kaur", room = "Lab 4", isPractical = false, isProgramming = true, credits = 3),
                BcaSubject(semester = 4, name = "Information Security", code = "BCA-404", teacher = "Dr. R. Bhat", room = "Room 406", isPractical = false, isProgramming = false, credits = 3),
                BcaSubject(semester = 4, name = "Python & Web Lab", code = "BCA-405", teacher = "Dr. A. Sen", room = "Computer Lab 4", isPractical = true, isProgramming = true, credits = 2)
            )
            5 -> listOf(
                BcaSubject(semester = 5, name = "Android App Development", code = "BCA-501", teacher = "Prof. K. Paul", room = "Lab 5", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 5, name = "Cloud Computing", code = "BCA-502", teacher = "Dr. G. Bose", room = "Room 501", isPractical = false, isProgramming = false, credits = 4),
                BcaSubject(semester = 5, name = "Machine Learning Basics", code = "BCA-503", teacher = "Dr. U. Rao", room = "Room 502", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 5, name = "Major Project Phase-I", code = "BCA-504", teacher = "Department Guide", room = "Project Lab", isPractical = true, isProgramming = true, credits = 3)
            )
            else -> listOf(
                BcaSubject(semester = 6, name = "Full Stack Web Development", code = "BCA-601", teacher = "Prof. T. Sen", room = "Room 601", isPractical = false, isProgramming = true, credits = 4),
                BcaSubject(semester = 6, name = "Software Testing & QA", code = "BCA-602", teacher = "Ms. L. Iyer", room = "Room 602", isPractical = false, isProgramming = false, credits = 3),
                BcaSubject(semester = 6, name = "Major Project Phase-II", code = "BCA-603", teacher = "Project Committee", room = "Project Lab", isPractical = true, isProgramming = true, credits = 6),
                BcaSubject(semester = 6, name = "Comprehensive Viva Voce", code = "BCA-604", teacher = "External Examiner", room = "Conference Hall", isPractical = true, isProgramming = false, credits = 2)
            )
        }
    }

    fun getSampleSyllabusTopics(cSubjectId: Long, mathSubjectId: Long, fundamentalsSubjectId: Long): List<SyllabusTopic> {
        val topics = mutableListOf<SyllabusTopic>()

        // C Programming syllabus units
        val cUnits = listOf(
            Pair("Unit 1: Introduction to C", listOf("History & Features of C", "Structure of C Program", "Data Types & Variables", "Constants & Keywords", "Operators & Expressions")),
            Pair("Unit 2: Control Statements", listOf("If and If-Else Statements", "Nested If & Else-If Ladder", "Switch Case Statement", "While & Do-While Loops", "For Loop & Jump Statements")),
            Pair("Unit 3: Arrays & Strings", listOf("1D Array Declaration & Init", "2D Arrays & Matrices", "String Handling Functions", "Character Arrays vs Strings", "Array Searching Algorithms")),
            Pair("Unit 4: Functions & Pointers", listOf("Function Prototypes & Definitions", "Call by Value vs Reference", "Recursion in C", "Pointer Syntax & Addresses", "Pointer Arithmetic & Dynamic Allocation")),
            Pair("Unit 5: Structures & File Handling", listOf("Structure Definition & Access", "Unions & Bit Fields", "File Pointers (fopen, fclose)", "Reading & Writing Files (fprintf, fscanf)", "Command Line Arguments"))
        )

        var sort = 0
        cUnits.forEachIndexed { uIdx, unit ->
            val uNum = uIdx + 1
            unit.second.forEachIndexed { tIdx, title ->
                val status = when {
                    uNum == 1 -> "COMPLETED"
                    uNum == 2 && tIdx <= 2 -> "COMPLETED"
                    uNum == 2 -> "IN_PROGRESS"
                    uNum == 3 && tIdx == 0 -> "IN_PROGRESS"
                    else -> "NOT_STARTED"
                }
                topics.add(
                    SyllabusTopic(
                        subjectId = cSubjectId,
                        semester = 1,
                        unitNumber = uNum,
                        unitTitle = unit.first,
                        topicTitle = title,
                        status = status,
                        sortOrder = sort++
                    )
                )
            }
        }

        // Discrete Mathematics
        val mathUnits = listOf(
            Pair("Unit 1: Set Theory", listOf("Set Operations & Laws", "Venn Diagrams", "Cartesian Products & Power Sets")),
            Pair("Unit 2: Relations & Functions", listOf("Equivalence Relations", "Partial Ordering", "Injective & Surjective Functions")),
            Pair("Unit 3: Propositional Logic", listOf("Tautology & Contradiction", "Truth Tables", "Logical Equivalence")),
            Pair("Unit 4: Graph Theory", listOf("Graph Terminology & Euler Paths", "Trees & Spanning Trees", "Dijkstra's Algorithm Basics"))
        )

        mathUnits.forEachIndexed { uIdx, unit ->
            val uNum = uIdx + 1
            unit.second.forEachIndexed { tIdx, title ->
                val status = when {
                    uNum == 1 -> "COMPLETED"
                    uNum == 2 && tIdx == 0 -> "IN_PROGRESS"
                    else -> "NOT_STARTED"
                }
                topics.add(
                    SyllabusTopic(
                        subjectId = mathSubjectId,
                        semester = 1,
                        unitNumber = uNum,
                        unitTitle = unit.first,
                        topicTitle = title,
                        status = status,
                        sortOrder = sort++
                    )
                )
            }
        }

        // Computer Fundamentals
        val compUnits = listOf(
            Pair("Unit 1: Hardware & Architecture", listOf("CPU Architecture & Bus System", "Primary & Secondary Storage", "Input/Output Interfaces")),
            Pair("Unit 2: Operating System & Software", listOf("Types of Software", "Role of OS & Process Basics", "File System Hierarchy")),
            Pair("Unit 3: Number Systems & Logic", listOf("Binary, Octal, Hex Conversions", "1's and 2's Complement", "Boolean Simplification"))
        )

        compUnits.forEachIndexed { uIdx, unit ->
            val uNum = uIdx + 1
            unit.second.forEachIndexed { tIdx, title ->
                val status = if (uNum <= 2) "COMPLETED" else "IN_PROGRESS"
                topics.add(
                    SyllabusTopic(
                        subjectId = fundamentalsSubjectId,
                        semester = 1,
                        unitNumber = uNum,
                        unitTitle = unit.first,
                        topicTitle = title,
                        status = status,
                        sortOrder = sort++
                    )
                )
            }
        }

        return topics
    }

    fun getSampleCodingExercises(cSubjectId: Long): List<CodingExercise> {
        return listOf(
            CodingExercise(subjectId = cSubjectId, topicCategory = "Variables", title = "Swap Two Numbers Without Third Variable", description = "Use arithmetic or XOR operator", isCompleted = true, notesOrSnippet = "a = a + b; b = a - b; a = a - b;"),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Data Types", title = "Find ASCII value of a character", description = "Input character and print %d", isCompleted = true, notesOrSnippet = "printf(\"%d\", ch);"),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Operators", title = "Find Greatest of 3 Numbers using Ternary", description = "Nested ternary condition", isCompleted = true),
            CodingExercise(subjectId = cSubjectId, topicCategory = "If-Else", title = "Check Leap Year", description = "Divisible by 4 and not 100, or divisible by 400", isCompleted = true),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Loops", title = "Check Armstrong Number", description = "Sum of cubes of digits equals number", isCompleted = false),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Loops", title = "Print Fibonacci Series up to N Terms", description = "Iterative and recursive loops", isCompleted = true),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Arrays", title = "Matrix Addition & Multiplication", description = "2D arrays with boundary checks", isCompleted = false),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Functions", title = "Factorial using Recursion", description = "Base condition n <= 1", isCompleted = true),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Pointers", title = "Reverse an Array using Pointers", description = "Two-pointer approach with dereferencing", isCompleted = false),
            CodingExercise(subjectId = cSubjectId, topicCategory = "Structures", title = "Student Record Management with Struct", description = "Store roll, name, marks for 5 students", isCompleted = false)
        )
    }

    fun getSampleLabExperiments(labSubjectId: Long): List<LabExperiment> {
        return listOf(
            LabExperiment(
                subjectId = labSubjectId,
                subjectName = "C Programming Lab",
                experimentNumber = 1,
                title = "Program to demonstrate basic I/O and conditional statements (Roots of quadratic equation)",
                isCompleted = true,
                fileStatus = "SIGNED",
                vivaStatus = "PREPARED"
            ),
            LabExperiment(
                subjectId = labSubjectId,
                subjectName = "C Programming Lab",
                experimentNumber = 2,
                title = "Program to perform Matrix Addition, Subtraction and Multiplication",
                isCompleted = true,
                fileStatus = "SIGNED",
                vivaStatus = "PREPARED"
            ),
            LabExperiment(
                subjectId = labSubjectId,
                subjectName = "C Programming Lab",
                experimentNumber = 3,
                title = "Implementation of Linear Search and Binary Search on sorted arrays",
                isCompleted = false,
                fileStatus = "IN_PROGRESS",
                vivaStatus = "NEEDS_REVISION"
            ),
            LabExperiment(
                subjectId = labSubjectId,
                subjectName = "C Programming Lab",
                experimentNumber = 4,
                title = "String manipulation programs (Palindrome, Length, Concatenation) without library functions",
                isCompleted = false,
                fileStatus = "PENDING",
                vivaStatus = "NOT_PREPARED"
            ),
            LabExperiment(
                subjectId = labSubjectId,
                subjectName = "C Programming Lab",
                experimentNumber = 5,
                title = "Program demonstrating call by value and call by reference using pointers",
                isCompleted = false,
                fileStatus = "PENDING",
                vivaStatus = "NOT_PREPARED"
            )
        )
    }

    fun getSampleTimetable(subjects: List<BcaSubject>): List<TimetableClass> {
        if (subjects.isEmpty()) return emptyList()
        val cProg = subjects.find { it.name.contains("C", ignoreCase = true) && !it.isPractical } ?: subjects[0]
        val math = subjects.find { it.name.contains("Math", ignoreCase = true) } ?: subjects.getOrElse(1) { subjects[0] }
        val comp = subjects.find { it.name.contains("Fundamentals", ignoreCase = true) } ?: subjects.getOrElse(2) { subjects[0] }
        val eng = subjects.find { it.name.contains("English", ignoreCase = true) || it.name.contains("Communication", ignoreCase = true) } ?: subjects.getOrElse(3) { subjects[0] }
        val lab = subjects.find { it.isPractical } ?: subjects.last()

        val list = mutableListOf<TimetableClass>()

        // Monday (1)
        list.add(TimetableClass(semester = 1, dayOfWeek = 1, subjectId = cProg.id, subjectName = cProg.name, teacher = cProg.teacher, room = cProg.room, startTime = "09:00", endTime = "10:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 1, subjectId = math.id, subjectName = math.name, teacher = math.teacher, room = math.room, startTime = "10:00", endTime = "11:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 1, subjectId = lab.id, subjectName = lab.name, teacher = lab.teacher, room = lab.room, startTime = "11:30", endTime = "13:30", isPractical = true))

        // Tuesday (2)
        list.add(TimetableClass(semester = 1, dayOfWeek = 2, subjectId = comp.id, subjectName = comp.name, teacher = comp.teacher, room = comp.room, startTime = "09:00", endTime = "10:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 2, subjectId = cProg.id, subjectName = cProg.name, teacher = cProg.teacher, room = cProg.room, startTime = "10:00", endTime = "11:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 2, subjectId = eng.id, subjectName = eng.name, teacher = eng.teacher, room = eng.room, startTime = "11:30", endTime = "12:30", isPractical = false))

        // Wednesday (3)
        list.add(TimetableClass(semester = 1, dayOfWeek = 3, subjectId = math.id, subjectName = math.name, teacher = math.teacher, room = math.room, startTime = "09:00", endTime = "10:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 3, subjectId = comp.id, subjectName = comp.name, teacher = comp.teacher, room = comp.room, startTime = "10:00", endTime = "11:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 3, subjectId = cProg.id, subjectName = cProg.name, teacher = cProg.teacher, room = cProg.room, startTime = "11:30", endTime = "12:30", isPractical = false))

        // Thursday (4)
        list.add(TimetableClass(semester = 1, dayOfWeek = 4, subjectId = cProg.id, subjectName = cProg.name, teacher = cProg.teacher, room = cProg.room, startTime = "10:00", endTime = "11:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 4, subjectId = lab.id, subjectName = lab.name, teacher = lab.teacher, room = lab.room, startTime = "11:30", endTime = "13:30", isPractical = true))

        // Friday (5)
        list.add(TimetableClass(semester = 1, dayOfWeek = 5, subjectId = math.id, subjectName = math.name, teacher = math.teacher, room = math.room, startTime = "09:00", endTime = "10:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 5, subjectId = comp.id, subjectName = comp.name, teacher = comp.teacher, room = comp.room, startTime = "10:00", endTime = "11:00", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 5, subjectId = eng.id, subjectName = eng.name, teacher = eng.teacher, room = eng.room, startTime = "11:30", endTime = "12:30", isPractical = false))

        // Saturday (6)
        list.add(TimetableClass(semester = 1, dayOfWeek = 6, subjectId = cProg.id, subjectName = cProg.name, teacher = cProg.teacher, room = cProg.room, startTime = "09:30", endTime = "10:30", isPractical = false))
        list.add(TimetableClass(semester = 1, dayOfWeek = 6, subjectId = math.id, subjectName = math.name, teacher = math.teacher, room = math.room, startTime = "10:30", endTime = "11:30", isPractical = false))

        return list
    }

    fun getSampleAssignments(cSubjectId: Long, mathSubjectId: Long): List<Assignment> {
        return listOf(
            Assignment(
                semester = 1,
                subjectId = cSubjectId,
                subjectName = "Programming in C",
                title = "C Assignment #2: Dynamic Memory & Pointers",
                description = "Solve 5 problems using malloc(), calloc() and free(). Submit handwritten code with output screenshots.",
                dueDate = "2026-09-15",
                priority = "HIGH",
                isCompleted = false
            ),
            Assignment(
                semester = 1,
                subjectId = mathSubjectId,
                subjectName = "Discrete Mathematics",
                title = "Set Theory & Relations Worksheet",
                description = "Complete exercises from Chapter 2 on equivalence relations and Hasse diagrams.",
                dueDate = "2026-09-18",
                priority = "MEDIUM",
                isCompleted = false
            ),
            Assignment(
                semester = 1,
                subjectId = cSubjectId,
                subjectName = "Programming in C",
                title = "Assignment #1: Control Flow and Loops",
                description = "Implement patterns and prime number logic.",
                dueDate = "2026-09-02",
                priority = "LOW",
                isCompleted = true
            )
        )
    }

    fun getSampleExams(cSubjectId: Long, mathSubjectId: Long, compSubjectId: Long): List<ExamEntry> {
        return listOf(
            ExamEntry(
                semester = 1,
                subjectId = cSubjectId,
                subjectName = "Programming in C",
                examType = "Mid-Sem",
                date = "2026-09-28",
                time = "10:00 AM",
                room = "Hall 204"
            ),
            ExamEntry(
                semester = 1,
                subjectId = mathSubjectId,
                subjectName = "Discrete Mathematics",
                examType = "Internal",
                date = "2026-10-05",
                time = "11:30 AM",
                room = "Room 205"
            ),
            ExamEntry(
                semester = 1,
                subjectId = compSubjectId,
                subjectName = "Computer Fundamentals",
                examType = "End-Sem",
                date = "2026-10-24",
                time = "02:00 PM",
                room = "Auditorium"
            )
        )
    }

    fun getSampleNotes(cSubjectId: Long, mathSubjectId: Long): List<QuickNote> {
        return listOf(
            QuickNote(
                semester = 1,
                subjectId = cSubjectId,
                subjectName = "Programming in C",
                category = "Programming",
                title = "Pointer vs Array Mechanics",
                content = "Array name acts as a constant pointer to its first element: *(arr + i) is identical to arr[i]. However, 'arr++' is illegal because arr is non-modifiable lvalue!"
            ),
            QuickNote(
                semester = 1,
                subjectId = mathSubjectId,
                subjectName = "Discrete Mathematics",
                category = "Mathematics",
                title = "Equivalence Relation Axioms",
                content = "A relation R on set A is an Equivalence Relation if and only if:\n1. Reflexive: (a, a) in R for all a\n2. Symmetric: (a, b) in R implies (b, a) in R\n3. Transitive: (a, b) in R and (b, c) in R implies (a, c) in R."
            ),
            QuickNote(
                semester = 1,
                category = "Labs",
                title = "Viva Voce Tips - C Programming",
                content = "Be ready to explain difference between struct & union (union shares memory of largest member), and storage classes: auto, register, static, extern."
            )
        )
    }
}
