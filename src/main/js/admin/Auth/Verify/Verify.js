import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useSearchParams } from 'react-router-dom'
import ApiClient from 'admin/utils/ApiClient'
import Loader from 'admin/Common/Loader'
import Login from 'admin/Auth/Login/Login'

const Verify = () => {
  const dispatch = useDispatch()
  const [searchParams] = useSearchParams()

  const { verifying, verifyResponse } = useSelector(state => ({
    verifying: state.verify?.loading || false,
    verifyResponse: state.verify?.value || {}
  }))

  useEffect(() => {
    dispatch({ type: 'VERIFY_LOADING' })

    const username = searchParams.get('username')
    const code = searchParams.get('code')
    const verificationUrl = `/auth/verify?username=${username}&code=${code}`

    ApiClient.get(verificationUrl).then(response => {
      dispatch({ type: 'VERIFY_LOADED', value: response })
    })
  }, [dispatch, searchParams])

  return (
    <section>
      <div id='verify-container'>
        <h2>Account Verification</h2>
        {verifying ? (
          <Loader />
        ) : verifyResponse.message ? (
          <>
            <p className='message'>{verifyResponse.message}</p>
            <Login />
          </>
        ) : (
          <p className='error'>{verifyResponse.error}</p>
        )}
      </div>
    </section>
  )
}

export default Verify