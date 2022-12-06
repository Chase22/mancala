package de.chasenet.macala.ui

import java.awt.Dimension
import javax.swing.JButton

class PitButton(text: String) : JButton(text) {
    init {
        size = Dimension(100, 100)
    }
}