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
    public constructor(applicationName: String, applicationIcon: Drawable) {
        this.applicationIcon = applicationIcon
        this.applicationName = applicationName
    }

    /*******************************************************************************************************************
    GETTERS AND SETTERS
     ******************************************************************************************************************/
    public fun getApplicationName() : String {
        return this.applicationName
    }
    public fun getApplicationIcon() : Drawable {
        return this.applicationIcon
    }
    public fun setApplicationName(applicationName: String) {
        this.applicationName = applicationName
    }
    public fun setApplicationIcon(applicationIcon: Drawable) {
        this.applicationIcon = applicationIcon
    }
}