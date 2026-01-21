import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from '../AuthHelper'
import FormError from 'admin/Form/FormError'
import Loader from 'admin/Common/Loader'

export default function Login() {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const [form, setForm] = useState({ emailOrUsername: '', password: '' })

  const { isLoggedIn, loading, error } = useSelector(state => ({
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loading: state.login?.loading || false,
    error: state.login?.error || null
  }))

  useEffect(() => {
    if (isLoggedIn) navigate('/ui')
  }, [isLoggedIn, navigate])

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
  }

  const login = async (emailOrUsername, password) => {
    dispatch({ type: 'LOGIN_LOADING' })
    try {
      const response = await ApiClient.post('/auth/login', { emailOrUsername, password })
      dispatch({ type: 'LOGIN_RESPONSE', value: response })
    } catch (err) {
      // Error handling here
    }
  }

  const handleLogin = (e) => {
    e.preventDefault()
    login(form.emailOrUsername, form.password)
  }

  const handleLoginAsGuest = () => {
    login('guest@matag.com', 'password')
  }

  return (
    <section id='login'>
      <div id='login-container'>
        <h2>Login</h2>
        <form className='matag-form' onSubmit={handleLogin}>
          <div className='grid grid-label-value'>
            <label htmlFor='email-or-username'>Email or Username: </label>
            <input type='text' id='email-or-username' name='email-or-username' value={form.emailOrUsername} onChange={handleChange}/>
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='password'>Password: </label>
            <input type='password' id='password' name='password' value={form.password} onChange={handleChange}/>
          </div>
          <FormError error={error} />
          <div className='grid three-columns'>
            <div />
            <div className='form-buttons'>
              <input type='submit' value='Login' />
              <div className='or'>or</div>
              <input type='button' value='Login as Guest' onClick={handleLoginAsGuest} />
            </div>
            {loading && <Loader center />}
          </div>
        </form>
      </div>
    </section>
  )
}
