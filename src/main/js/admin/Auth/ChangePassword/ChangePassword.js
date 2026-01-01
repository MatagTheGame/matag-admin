import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import ApiClient from 'admin/utils/ApiClient'
import history from 'admin/utils/history'
import AuthHelper from '../AuthHelper'
import AbstractForm from 'admin/utils/AbstractForm'

class ChangePassword extends AbstractForm {
  constructor(props) {
    super(props)
    this.state = {
      oldPassword: '',
      newPassword: ''
    }
    this.handleChangeOldPassword = this.handleChangeOldPassword.bind(this)
    this.handleChangeNewPassword = this.handleChangeNewPassword.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChangeOldPassword(event) {
    this.setState({oldPassword: event.target.value})
  }

  handleChangeNewPassword(event) {
    this.setState({newPassword: event.target.value})
  }

  submit(oldPassword, newPassword) {
    const request = {
      oldPassword: oldPassword,
      newPassword: newPassword
    }

    this.props.changePasswordLoading()
    ApiClient.post('/auth/change-password', request)
      .then(response => this.props.changePasswordResponse(response))
  }

  handleSubmit(event) {
    event.preventDefault()
    this.submit(this.state.oldPassword, this.state.newPassword)
  }

  render() {
    if (!this.props.isNonGuest) {
      history.push('/ui')
    }

    return (
      <section id='change-password'>
        <div id='change-password-container'>
          <h2>Change Password</h2>
          <form className='matag-form' onSubmit={this.handleSubmit}>
            <div className='grid grid-label-value'>
              <label htmlFor='old-password'>Old password: </label>
              <input type='password' id='old-password' name='old-password' value={this.state.oldPassword} onChange={this.handleChangeOldPassword}/>
            </div>
            <div className='grid grid-label-value'>
              <label htmlFor='new-password'>New password: </label>
              <input type='password' id='new-password' name='new-password' value={this.state.newPassword} onChange={this.handleChangeNewPassword}/>
            </div>
            {this.displayError()}
            {this.displayMessage()}
            <div className='grid three-columns'>
              <div/>
              <div className='form-buttons'>
                <input type='submit' value='Change Password'/>
              </div>
              {this.displayLoader()}
            </div>
          </form>
        </div>
      </section>
    )
  }
}

const changePasswordLoading = () => {
  return {
    type: 'CHANGE_PASSWORD_LOADING'
  }
}

const changePasswordResponse = (response) => {
  return {
    type: 'CHANGE_PASSWORD_RESPONSE',
    value: response
  }
}

const mapStateToProps = state => {
  return {
    isNonGuest: AuthHelper.isNonGuest(state),
    loading: get(state, 'changePassword.loading', false),
    message: get(state, 'changePassword.value.message', null),
    error: get(state, 'changePassword.value.error', null)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    changePasswordLoading: bindActionCreators(changePasswordLoading, dispatch),
    changePasswordResponse: bindActionCreators(changePasswordResponse, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(ChangePassword)
