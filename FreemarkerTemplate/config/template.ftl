{
  "student": {
    "id": 1,
    "student_username": "${student.userName}",
    "name": {
      "first_name": "${student.firstName}",
      "last_name": "${student.lastName}"
    },
    "phones": {
      "main": {
        "number": "${student.phone}"
      }
    },
    "contact_info": {
      "email": "${student.email}"
    }
  }
}