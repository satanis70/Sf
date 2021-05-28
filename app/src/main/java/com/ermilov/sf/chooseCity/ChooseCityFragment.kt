 package com.ermilov.sf.chooseCity


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.ermilov.sf.R
import com.ermilov.sf.account.room.CityDatabase
import com.ermilov.sf.account.room.CityViewModel
import com.ermilov.sf.account.room.RoomModelCity
import com.ermilov.sf.adopenfromRecycler.AdOpenInfoArgs
import kotlinx.android.synthetic.main.choose_city_fragment.*


class ChooseCityFragment : Fragment() {

    val args : ChooseCityFragmentArgs by navArgs()
    lateinit var navController: NavController
    lateinit var cityviewmodel: CityViewModel
    var selectedItem: String = String()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cityviewmodel = ViewModelProvider(this).get(CityViewModel::class.java)
        return inflater.inflate(R.layout.choose_city_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityviewmodel = ViewModelProvider(this).get(CityViewModel::class.java)
        navController = Navigation.findNavController(view)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, resources.getStringArray(R.array.cities))
        autoCompleteEditTextCity.setAdapter(adapter)
        autoCompleteEditTextCity.showDropDown()
        autoCompleteEditTextCity.onItemClickListener = AdapterView.OnItemClickListener{
            parent,_view,position,id->
            selectedItem  = parent.getItemAtPosition(position).toString()
            view.hideKeyboard()
        }




        buttonNext.setOnClickListener {

            insertCiryToDatabase()
        }


    }

    fun insertCiryToDatabase(){
        if (selectedItem.isNotEmpty()){
            cityviewmodel.addCity(RoomModelCity(1, selectedItem))
            Toast.makeText(requireContext(),selectedItem,Toast.LENGTH_SHORT).show()
            view?.hideKeyboard()

            if (args.from == "account"){
                navController.navigate(R.id.action_choose_city_fragment_to_fragment_account)
                autoCompleteEditTextCity.setText("")
            } else {
                navController.navigate(R.id.choose_city_fragment_to_registration_fragment)
                autoCompleteEditTextCity.setText("")
            }

        } else {
            Toast.makeText(requireContext(), R.string.choose_city_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun View.hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}