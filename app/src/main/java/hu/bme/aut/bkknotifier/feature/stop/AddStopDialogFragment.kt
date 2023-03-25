package hu.bme.aut.bkknotifier.feature.stop

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.DialogNewStopBinding
import hu.bme.aut.bkknotifier.feature.stop.data.Stop

class AddStopDialogFragment : AppCompatDialogFragment() {

    private lateinit var binding: DialogNewStopBinding
    private lateinit var listener: AddStopDialogListener

    interface AddStopDialogListener {
        fun onStopCreated(stop: Stop)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewStopBinding.inflate(LayoutInflater.from(context))

        listener = context as? AddStopDialogListener
            ?: throw RuntimeException("Activity must implement the AddStopDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_Stop)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                val name = binding.NewStopDialogNameEditText.text.toString()
                val id = binding.NewStopDialogIdEditText.text.toString()
                if(name != "" && id != ""){
                    listener.onStopCreated(
                        Stop(name, false, id)
                    )
                } else {
                    Toast.makeText(context, getString(R.string.invalid_form), Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}