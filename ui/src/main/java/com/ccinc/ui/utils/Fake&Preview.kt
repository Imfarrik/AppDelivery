package com.ccinc.ui.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ccinc.data.model.Categories

//@PreviewParameter(PrayTimePreviewProvider::class)

class PrayTimePreviewProvider : PreviewParameterProvider<Categories> {
    override val values: Sequence<Categories>
        get() = sequenceOf(Categories.fake())
}