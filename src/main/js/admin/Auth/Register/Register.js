import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import ApiClient from 'admin/utils/ApiClient'
import history from 'admin/utils/history'
import AuthHelper from '../AuthHelper'
import AbstractForm from 'admin/utils/AbstractForm'

class Register  extends AbstractForm {
  constructor(props) {
    super(props)
    this.state = {
      email: '',
      username: '',
      password: ''
    }
    this.handleChangeEmail = this.handleChangeEmail.bind(this)
    this.handleChangeUsername = this.handleChangeUsername.bind(this)
    this.handleChangePassword = this.handleChangePassword.bind(this)
    this.handleRegister = this.handleRegister.bind(this)
  }

  handleChangeEmail(event) {
    this.setState({email: event.target.value})
  }

  handleChangeUsername(event) {
    this.setState({username: event.target.value})
  }

  handleChangePassword(event) {
    this.setState({password: event.target.value})
  }

  register(email, username, password) {
    const request = {
      email: email,
      username: username,
      password: password
    }

    this.props.registerLoading()
    ApiClient.post('/auth/register', request)
      .then(response => this.props.registerResponse(response))
  }

  handleRegister(event) {
    event.preventDefault()
    this.register(this.state.email, this.state.username, this.state.password)
  }

  render() {
    if (this.props.isLoggedIn) {
      history.push('/ui/admin')
    }

    return (
      <section id='register'>
        <div id='register-container'>
          <h2>Register</h2>
          <form className='matag-form' onSubmit={this.handleRegister}>
            <div className='grid grid-label-value'>
              <label htmlFor='email'>Email: </label>
              <input type='text' id='email' name='email' value={this.state.email} onChange={this.handleChangeEmail}/>
            </div>
            <div className='grid grid-label-value'>
              <label htmlFor='username'>Username: </label>
              <input type='text' id='username' name='username' value={this.state.username} onChange={this.handleChangeUsername}/>
            </div>
            <div className='grid grid-label-value'>
              <label htmlFor='password'>Password: </label>
              <input type='password' id='password' name='password' value={this.state.password} onChange={this.handleChangePassword}/>
            </div>
            {this.displayError()}
            {this.displayMessage()}
            <div className='grid three-columns'>
              <div/>
              <div className='form-buttons'>
                <input type='submit' value='Register'/>
              </div>
              {this.displayLoader()}
            </div>
          </form>
        </div>
      </section>
    )
  }
}

const registerLoading = () => {
  return {
    type: 'REGISTER_LOADING'
  }
}

const registerResponse = (response) => {
  return {
    type: 'REGISTER_RESPONSE',
    value: response
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loading: get(state, 'register.loading', false),
    message: get(state, 'register.value.message', null),
    error: get(state, 'register.value.error', null)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    registerLoading: bindActionCreators(registerLoading, dispatch),
    registerResponse: bindActionCreators(registerResponse, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Register)
