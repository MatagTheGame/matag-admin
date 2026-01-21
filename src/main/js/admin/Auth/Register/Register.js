import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from '../AuthHelper'
import FormError from 'admin/Form/FormError'
import FormMessage from 'admin/Form/FormMessage'
import Loader from 'admin/Common/Loader'

const Register = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const [form, setForm] = useState({ email: '', username: '', password: '' })

  const { isLoggedIn, loading, message, error } = useSelector(state => ({
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loading: state.register?.loading || false,
    message: state.register?.value?.message || null,
    error: state.register?.value?.error || null
  }))

  useEffect(() => {
    if (isLoggedIn) navigate('/ui')
  }, [isLoggedIn, navigate])

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
  }

  const handleRegister = async (e) => {
    e.preventDefault()
    dispatch({ type: 'REGISTER_LOADING' })
    try {
      const response = await ApiClient.post('/auth/register', form)
      dispatch({ type: 'REGISTER_RESPONSE', value: response })
    } catch (err) {
      // Error handled by reducer via response
    }
  }

  return (
    <section id='register'>
      <div id='register-container'>
        <h2>Register</h2>
        <form className='matag-form' onSubmit={handleRegister}>
          <div className='grid grid-label-value'>
            <label htmlFor='email'>Email: </label>
            <input type='text' id='email' name='email' value={form.email} onChange={handleChange} />
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='username'>Username: </label>
            <input type='text' id='username' name='username' value={form.username} onChange={handleChange} />
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='password'>Password: </label>
            <input type='password' id='password' name='password' value={form.password} onChange={handleChange} />
          </div>

          <FormError error={error} />
          <FormMessage message={message} />

          <div className='grid three-columns'>
            <div />
            <div className='form-buttons'>
              <input type='submit' value='Register' />
            </div>
            {loading && <Loader center />}
          </div>

          <p>
            <u>Note:</u> if you are having trouble, drop an email at
            <strong> matag.the.game@gmail.com </strong>
            and someone will help.
          </p>
        </form>
      </div>
    </section>
  )
}

export default Register