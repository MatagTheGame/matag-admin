import React from 'react'
import {Link, useNavigate} from 'react-router-dom'
import AuthHelper from 'admin/Auth/AuthHelper'
import ApiClient from 'admin/utils/ApiClient'
import {APP_BASE_PATH} from 'admin/AdminApp'

export default function Logout() {
  const navigate = useNavigate()

  const handleLogout = () => {
    ApiClient.getNoJson('/auth/logout')
      .then(() => {
        AuthHelper.removeToken()
        navigate(APP_BASE_PATH + '/ui')
      })
  }

  return (
    <Link to="#" onClick={handleLogout}>Logout</Link>
  )
}
