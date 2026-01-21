import React, { useState, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from '../AuthHelper'
import FormMessage from 'admin/Form/FormMessage'
import FormError from 'admin/Form/FormError'
import Loader from 'admin/Common/Loader'

const ChangePassword = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const [form, setForm] = useState({ oldPassword: '', newPassword: '' })

  const { isNonGuest, loading, message, error } = useSelector(state => ({
    isNonGuest: AuthHelper.isNonGuest(state),
    loading: state.changePassword?.loading || false,
    message: state.changePassword?.value?.message || null,
    error: state.changePassword?.value?.error || null
  }))

  useEffect(() => {
    if (!isNonGuest) navigate('/ui')
  }, [isNonGuest, navigate])

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    dispatch({ type: 'CHANGE_PASSWORD_LOADING' })
    try {
      const response = await ApiClient.post('/auth/change-password', form)
      dispatch({ type: 'CHANGE_PASSWORD_RESPONSE', value: response })
    } catch (err) {
      // Error handled by reducer
    }
  }

  return (
    <section id='change-password'>
      <div id='change-password-container'>
        <h2>Change Password</h2>
        <form className='matag-form' onSubmit={handleSubmit}>
          <div className='grid grid-label-value'>
            <label htmlFor='old-password'>Old password: </label>
            <input type='password' id='old-password' name='old-password' value={form.oldPassword} onChange={handleChange}/>
          </div>
          <div className='grid grid-label-value'>
            <label htmlFor='new-password'>New password: </label>
            <input type='password' id='new-password' name='new-password' value={form.newPassword} onChange={handleChange}/>
          </div>

          <FormError error={error} />
          <FormMessage message={message} />

          <div className='grid three-columns'>
            <div />
            <div className='form-buttons'>
              <input type='submit' value='Change Password' disabled={loading} />
            </div>
            {loading && <Loader center />}
          </div>
        </form>
      </div>
    </section>
  )
}

export default ChangePassword