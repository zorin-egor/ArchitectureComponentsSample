package com.sample.architecturecomponents.core.database.converters

import com.google.gson.reflect.TypeToken
import com.sample.architecturecomponents.core.model.License

internal class LicenseConverter : ObjectConverter<License>(
    typeToken = TypeToken.get(License::class.java)
)