package com.shin.movieapp.ui.user

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.inflate
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.shin.movieapp.R
import com.shin.movieapp.databinding.ActivityEditUserBinding
import com.shin.movieapp.model.user.User
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.repository.user.UserRepository
import com.shin.movieapp.utils.RealPathUtil
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class EditUserActivity : AppCompatActivity() {
    private val IMAGE_PICK_CODE = 100
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var userViewModel: UserViewModel
    var selectedImage: String? = null

    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        val userDao = UserDatabase.getInstance(application).userDao()
        val userRepository = UserRepository(userDao)

        val factory = UserViewModelFactory(userRepository,application)

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)

        binding.viewmodel = userViewModel

        binding.lifecycleOwner = this

        // Birthday
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.birthDayEditText.setOnClickListener{
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    binding.birthDayEditText.text = "${year}-${monthOfYear+1}-${dayOfMonth}"
                }, year, month, day)
            dpd.show()
        }
        // cancel
        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }
        // save
        binding.doneButton.setOnClickListener {
            userViewModel.updateUser(binding.birthDayEditText.text as String,
                binding.maleRadioButton.isChecked, selectedImage.toString())
            finish()
        }
        // pick image
        binding.profileImage.setOnClickListener{
            pickImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.profileImage.setImageURI(data?.data)
            selectedImage = data.getFilePath(this)
            Log.i("Image path", "${data.getFilePath(this)}")
        }
    }

    fun pickImage(){
        // Pick image
        binding.profileImage.setOnClickListener{
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    fun Uri?.getFilePath(context: Context): String {
        return this?.let { uri -> RealPathUtil.getRealPath(context, uri) ?: "" } ?: ""
    }

    fun Intent?.getFilePath(context: Context): String {
        return this?.data?.let { data -> RealPathUtil.getRealPath(context, data) ?: "" } ?: ""
    }
}