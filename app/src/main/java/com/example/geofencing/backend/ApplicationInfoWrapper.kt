package com.example.geofencing.backend

import android.graphics.drawable.Drawable

class ApplicationInfoWrapper {
    /*******************************************************************************************************************
    VARIABLE DECLARATION
     ******************************************************************************************************************/
    private var applicationName: String
    private var applicationIcon: Drawable

    /*******************************************************************************************************************
    CONSTRUCTORS
     ******************************************************************************************************************/
    constructor(applicationName: String, applicationIcon: Drawable) {
        this.applicationIcon = applicationIcon
        this.applicationName = applicationName
    }

    /*******************************************************************************************************************
    GETTERS AND SETTERS
     ******************************************************************************************************************/
    fun getApplicationName(): String {
        return this.applicationName
    }

    fun getApplicationIcon(): Drawable {
        return this.applicationIcon
    }

    fun setApplicationName(applicationName: String) {
        this.applicationName = applicationName
    }

    fun setApplicationIcon(applicationIcon: Drawable) {
        this.applicationIcon = applicationIcon
    }
}