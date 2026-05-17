package dev.LeadRDRK.UmaPatcherEdge

import android.app.Application
import dev.LeadRDRK.UmaPatcherEdge.shizuku.ShizukuState

class UmaPatcherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ShizukuState.init()
    }
}
