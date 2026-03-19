package com.danylom73.rescuehelper.domain.requirement

import androidx.annotation.StringRes
import com.danylom73.rescuehelper.R

enum class RequirementCategory(@StringRes val titleRes: Int) {
    Permission(R.string.requirement_permission_category_title),
    Feature(R.string.requirement_feature_category_title)
}