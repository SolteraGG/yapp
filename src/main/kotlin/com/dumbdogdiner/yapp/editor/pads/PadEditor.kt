package com.dumbdogdiner.yapp.editor.pads

import com.dumbdogdiner.yapp.editor.EditingSession
import com.dumbdogdiner.yapp.structures.Pad

/**
 * An editor for editing pads (not surprisingly).
 */
interface PadEditor {
    val pad: Pad
    val session: EditingSession

    /**
     * End the current pad editing session.
     */
    fun endPadEditingSession(): PadEditor
}