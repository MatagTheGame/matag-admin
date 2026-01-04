import React from 'react'
import {Link, useNavigate} from 'react-router-dom'
import AuthHelper from 'admin/Auth/AuthHelper'
import ApiClient from 'admin/utils/ApiClient'

export default function Logout() {
  const navigate = useNavigate()

  const handleLogout = () => {
    ApiClient.getNoJson('/auth/logout')
      .then(() => {
        AuthHelper.removeToken()
        navigate('/')
        window.location.reload()
      })
  }

  return (
    <Link to="#" onClick={handleLogout}>Logout</Link>
  )
}
