package io.github.metalcupcake5.intentslearning

data class Grade(
    var assignment: String = "Assignment",
    var enjoyedAssignment: Boolean = true,
    var letterGradeValue: Int = 4,
    var percentage: Double = 1.0,
    var studentName: String = "Joe",
    var subject: String = "Default Subject",
    var objectId : String? = null,
    var ownerId : String = ""
)
