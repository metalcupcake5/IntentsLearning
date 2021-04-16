package io.github.metalcupcake5.intentslearning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_grade_list.*


class GradeListActivity : AppCompatActivity() {
    companion object {
        val TAG = "GradleListActivity"
    }

    private lateinit var userId : String
    private var gradesList : List<Grade?>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_list)
        userId = Backendless.UserService.CurrentUser().userId
        readAllUserGrades()

        button_gradelist_read.setOnClickListener {
            readAllUserGrades()
            Toast.makeText(this@GradeListActivity, "Grades Read", Toast.LENGTH_SHORT).show();
        }

        button_gradelist_create.setOnClickListener {
            createNewGrade()
        }

        button_gradelist_update.setOnClickListener {
            updateFirstGrade()
        }

        button_gradelist_delete.setOnClickListener {
            deleteFirstGrade()
        }
    }

    private fun createNewGrade() {
        Log.d(TAG, "createNewGrade: $userId")
        val grade = Grade(assignment = "Chapter 5 Multiple Choice", studentName = "Alden")
        grade.ownerId = userId
        Log.d(TAG, "createNewGrade: ")
        Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
            override fun handleResponse(response: Grade?) {
                Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show();
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: ${fault.detail}")
            }
        })
    }

    private fun readAllUserGrades() {
        // do an advanced data retrieval with a where clause that matches the ownerId to current userId
        // ownerId = 'blah'   but 'blah' is our variable for the userId
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        Backendless.Data.of(Grade::class.java).find(queryBuilder,
            object : AsyncCallback<List<Grade?>?> {
                override fun handleResponse(foundGrades: List<Grade?>?) {
                    // the "foundGrades" collection now contains instances of the Grade class.
                    // each instance represents an object stored on the server.
                    gradesList = foundGrades
                    Log.d(TAG, "handleResponse: " + foundGrades.toString())
                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d(TAG, "handleFault: " + fault.message)
                }
            })
    }

    private fun deleteFirstGrade(){
        if(!gradesList.isNullOrEmpty()) {
            val grade = gradesList?.first()
            Backendless.Data.of(Grade::class.java).remove(grade,
                object : AsyncCallback<Long> {
                        override fun handleResponse(response: Long?) {
                            // Contact objectdhas been deleted
                            Toast.makeText(this@GradeListActivity, "Grade Deleted", Toast.LENGTH_SHORT).show();
                            readAllUserGrades()
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                            Log.d(TAG, "handleFault: " + fault.message)
                        }
                })
        }
    }

    private fun updateFirstGrade() {
        if(!gradesList.isNullOrEmpty()) {
            val grade = gradesList?.first()
            grade?.assignment = "new and improved item"
            Backendless.Data.of(Grade::class.java).save(grade,
                object : AsyncCallback<Grade?> {
                    override fun handleResponse(response: Grade?) {
                        // Contact objectdhas been updated
                        Toast.makeText(this@GradeListActivity, "Grade Updated", Toast.LENGTH_SHORT).show();
                        readAllUserGrades()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Log.d(TAG, "handleFault: " + fault.message)
                    }
                })
        }
    }

}