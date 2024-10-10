package com.raman.verma.tripmmapp

import android.app.AlertDialog
import android.content.Context

object Utils {
    fun showDeleteConfirmationDialog(context: Context, title:String, message:String, onConfirm: () -> Unit, onCancel: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)


        builder.setPositiveButton("Yes") { dialog, _ ->
            onConfirm() // Trigger the confirm action
            dialog.dismiss() // Close the dialog
        }

        // Set up the negative button (No)
        builder.setNegativeButton("No") { dialog, _ ->
            onCancel()
            dialog.dismiss() // Just close the dialog
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}