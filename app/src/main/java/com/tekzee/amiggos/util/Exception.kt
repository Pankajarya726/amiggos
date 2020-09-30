package com.tekzee.amiggos.util

import java.io.IOException
import java.lang.Exception

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String): IOException(message)
class LogoutException(message: String): Exception(message)