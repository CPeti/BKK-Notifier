package hu.bme.aut.bkknotifier.feature.details.alert

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.bkknotifier.R
import hu.bme.aut.bkknotifier.databinding.DialogNewAlertBinding
import hu.bme.aut.bkknotifier.feature.stop.data.Alert

class AddAlertDialogFragment : AppCompatDialogFragment() {

    private lateinit var binding: DialogNewAlertBinding
    private lateinit var listener: AddAlertDialogListener

    interface AddAlertDialogListener {
        fun onAlertCreated(alert: Alert)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogNewAlertBinding.inflate(LayoutInflater.from(context))

        listener = parentFragment as? AddAlertDialogListener
            ?: throw RuntimeException("Activity must implement the AddAlertDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_Alert)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { _, _ ->
                val shortname = binding.NewAlertDialogShortnameEditText.text.toString()
                var time = binding.NewAlertDialogTimeEditText.text.toString()
                if(time == ""){
                    time = "0"
                }
                if(shortname != ""){
                    listener.onAlertCreated(
                        Alert(shortname, time.toInt())
                    )
                } else {
                    Toast.makeText(context, getString(R.string.invalid_form), Toast.LENGTH_LONG).show()
                }

            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}