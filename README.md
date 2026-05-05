# 🎓 UNIVERSITY MANAGEMENT SYSTEM

**Course:** Object-Oriented Programming  
**Semester:** 2nd Semester Final Project  
**Author:** [Hasnain Raza] and [Abdullah Shah]  
**Date:** May 2026


---

## 📋 PROJECT OVERVIEW

A comprehensive University Management System built using Java that demonstrates core OOP concepts including inheritance, abstraction, encapsulation, and polymorphism. The system manages students, faculty, administrators, courses, departments, and academic records with file-based persistence.

---

## 🎯 FEATURES

### ✅ Core Functionality
- **User Authentication System**
  - Secure login and signup
  - Role-based access (Admin, Student, Faculty)
  - File-based user data persistence
  
- **Student Management**
  - Course enrollment
  - GPA calculation
  - Transcript generation
  - Grade tracking

- **Faculty Management**
  - Course assignments
  - Department affiliation
  - Contact information

- **Department Management**
  - Course catalog
  - Faculty roster
  - Department information

- **Academic Records**
  - Course information
  - Grade management
  - Exam scheduling

---

## 🏗️ SYSTEM ARCHITECTURE

### Package Structure
```
src/
├── model/          # Domain entities
├── dao/            # Data Access Objects (File I/O)
├── service/        # Business logic
├── main/           # Application entry point
├── test/           # Test classes
└── filehandling/   # Data storage
```

---

## 📦 CLASSES OVERVIEW

### Model Layer (Domain Objects)

#### **1. Person (Abstract Base Class)**
- **Purpose:** Common attributes for all people in the system
- **Fields:** name, id, email, phone
- **Methods:** Abstract `getDetails()`
- **Subclasses:** Student, Faculty, Admin

#### **2. Student extends Person**
- **Additional Fields:** regId, semester, department, courseList, gradesList
- **Key Methods:**
  - `enrollCourse()` - Enroll in a course
  - `getGPA()` - Calculate GPA from grades
  - `viewTranscript()` - Display academic transcript

#### **3. Faculty extends Person**
- **Additional Fields:** employeeId, department, coursesTaught
- **Key Methods:**
  - `assignCourse()` - Assign teaching courses

#### **4. Admin extends Person**
- **Additional Fields:** adminId, designation
- **Purpose:** Administrative staff management

#### **5. Course**
- **Fields:** courseId, courseName, creditHours, instructor
- **Purpose:** Course catalog management

#### **6. Department**
- **Fields:** departmentId, departmentName, courses, facultyMembers
- **Methods:**
  - `addCourse()` - Add course to department
  - `addFaculty()` - Add faculty to department

#### **7. Grade**
- **Fields:** course, letterGrade, gradePoint
- **Purpose:** Student grade tracking

#### **8. Exam**
- **Fields:** examId, course, examDate, examType, totalMarks
- **Purpose:** Exam scheduling and management

#### **9. User**
- **Fields:** username, password, role
- **Purpose:** Authentication credentials

---

### DAO Layer (Data Access)

#### **UserDAO**
- **Purpose:** Handle user data persistence
- **Methods:**
  - `getAllUsers()` - Load users from file
  - `saveUser()` - Save users to file
- **File Format:** `username,password,role`

---

### Service Layer (Business Logic)

#### **AuthService**
- **Purpose:** Handle authentication operations
- **Methods:**
  - `login()` - Authenticate users
  - `signUp()` - Register new users

#### **AuthServiceImproved** (Enhanced Version)
- **Additional Features:**
  - Input validation
  - Password strength checking
  - Better error messages
  - Role verification

---

## 🔑 OOP CONCEPTS DEMONSTRATED

### 1. **Inheritance**
```
Person (Abstract)
  ├── Student
  ├── Faculty
  └── Admin
```

### 2. **Abstraction**
- `Person` class is abstract with abstract method `getDetails()`
- Each subclass provides its own implementation

### 3. **Encapsulation**
- All fields are private
- Access through public getters/setters
- Data hiding principle

### 4. **Polymorphism**
```java
Person[] people = {new Student(...), new Faculty(...), new Admin(...)};
for(Person p : people) {
    System.out.println(p.getDetails()); // Runtime polymorphism
}
```

---

## 🚀 HOW TO RUN

### Prerequisites
- Java JDK 8 or higher
- Any Java IDE (Eclipse, IntelliJ, NetBeans) or command line

### Compilation
```bash
# Navigate to project directory
cd university-system

# Compile all files
javac -d bin src/model/*.java src/dao/*.java src/service/*.java src/main/*.java src/test/*.java

# Run main program
java -cp bin main.Main

# Or run tests
java -cp bin test.TestSystem
```

### Using an IDE
1. Import project into your IDE
2. Set `src` as source folder
3. Run `main.Main` class

---

## 📝 USAGE GUIDE

### First Time Setup
1. Run the program
2. Choose option 2 (Sign Up)
3. Create an account with:
   - Username (minimum 3 characters)
   - Password (minimum 6 characters)
   - Role (Admin/Student/Faculty)

### Login
1. Choose option 1 (Login)
2. Enter your username and password
3. Enter your role

### Example Session
```
========================================
   UNIVERSITY MANAGEMENT SYSTEM
========================================

----- MENU -----
1. Login
2. Sign Up
3. Exit
Choose an option: 2

Enter username: ahmad123
Enter password: securepass
Enter role (Admin/Student/Faculty): Student
✓ Account created successfully!

----- MENU -----
1. Login
2. Sign Up
3. Exit
Choose an option: 1

Enter your username: ahmad123
Enter your password: securepass
Enter your role: Student
✓ Login Successfully.
```

---

## 🧪 TESTING

### Run Test Suite
```bash
java -cp bin test.TestSystem
```

### Test Coverage
- ✅ Model class instantiation
- ✅ Inheritance chain
- ✅ Polymorphism
- ✅ File I/O operations
- ✅ Authentication flow

### Manual Testing Scenarios

#### Test 1: New User Signup
```
Input: ahmad123, pass123, Student
Expected: Account created successfully
Verify: Check users.txt file
```

#### Test 2: Successful Login
```
Input: ahmad123, pass123, Student
Expected: Login Successfully
```

#### Test 3: Wrong Password
```
Input: ahmad123, wrongpass, Student
Expected: Please enter correct password
```

#### Test 4: Wrong Role
```
Input: ahmad123, pass123, Admin
Expected: Incorrect role
```

#### Test 5: Non-existent User
```
Input: ghost, 123, Student
Expected: User not found
```

#### Test 6: Duplicate Username
```
Input: ahmad123 (already exists), newpass, Student
Expected: User already exist
```

---

## 📊 DATA PERSISTENCE

### File Storage
- **Location:** `src/filehandling/users.txt`
- **Format:** CSV (Comma-Separated Values)
- **Structure:** `username,password,role`

### Example Data
```
ahmad123,pass123,Student
dr.khan,securepass,Faculty
admin1,admin123,Admin
```

---

## 🔒 SECURITY NOTES

### Current Implementation
- ⚠️ Passwords stored in plain text
- ⚠️ Basic validation only
- ⚠️ No password recovery mechanism

### Future Improvements
- 🔐 Implement password hashing (BCrypt/SHA-256)
- 🔐 Add password strength requirements
- 🔐 Implement session management
- 🔐 Add password recovery
- 🔐 Implement account lockout after failed attempts

---

## 🎓 LEARNING OUTCOMES

This project demonstrates understanding of:

1. **Object-Oriented Design**
   - Class hierarchy design
   - Proper use of inheritance
   - Abstract classes and methods

2. **File I/O Operations**
   - Reading from files
   - Writing to files
   - Data persistence

3. **Exception Handling**
   - Try-catch blocks
   - IOException handling

4. **Data Structures**
   - HashMap for user storage
   - ArrayList for collections

5. **Design Patterns**
   - DAO (Data Access Object) pattern
   - Service layer pattern

---

## 📈 POSSIBLE EXTENSIONS

### Phase 2 Features
- [ ] Admin dashboard
- [ ] Course registration system
- [ ] Attendance tracking
- [ ] Fee management
- [ ] Report generation

### Phase 3 Features
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] GUI using JavaFX/Swing
- [ ] Password encryption
- [ ] Email notifications
- [ ] Student portal

---

## 🐛 KNOWN ISSUES

None - All critical bugs have been fixed!

---

## 📞 TROUBLESHOOTING

### Issue: "File not Found Exception"
**Solution:** Ensure `src/filehandling/users.txt` exists

### Issue: Compilation errors
**Solution:** Make sure all .java files are in correct packages

### Issue: Users not saving
**Solution:** Check file permissions and path

---

## 👨‍💻 CODE QUALITY

### Best Practices Followed
- ✅ Proper naming conventions
- ✅ Code comments and documentation
- ✅ Exception handling
- ✅ Separation of concerns
- ✅ DRY principle (Don't Repeat Yourself)
- ✅ Single Responsibility Principle

---

## 📜 LICENSE

Educational Project - Free to use for learning purposes

---

## 🙏 ACKNOWLEDGMENTS

- Course Instructor
- Java Documentation
- Stack Overflow Community

---

## 📧 CONTACT

For questions or issues, contact: [chandiohasnain807@gmail.com] [syedabdullahshah086@gmail.com]

---

**Project Status:** ✅ Complete and Ready for Submission

**Last Updated:** May 2026
