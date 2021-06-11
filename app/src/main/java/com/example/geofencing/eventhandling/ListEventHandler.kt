package com.example.geofencing.eventhandling

import android.content.Context
import android.view.View
import com.example.geofencing.R
import com.example.geofencing.backend.ApplicationInfoWrapper

class ListEventHandler: View.OnClickListener {
    val reference: Context
    val applicationsMap: Map<String, ApplicationInfoWrapper>

    constructor(reference: Context) {
        this.reference = reference
        this.applicationsMap = HashMap()
    }

    override fun onClick(view: View?) {
        val id = view?.id
        if(id == R.id.nextActionButton) {
            // Perform the tasks that needs to be performed when the floating action button is clicked....
        } else {
            // Perform the tasks that needs to be performed when any of the item in the list is clicked....
        }
    }
}