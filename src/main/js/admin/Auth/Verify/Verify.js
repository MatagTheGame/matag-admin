import React, {Component} from 'react'
import {connect} from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import Loader from 'admin/Common/Loader'
import Login from 'admin/Auth/Login/Login'

class Verify extends Component {
  componentDidMount() {
    this.props.verifyLoading()
    let verificationUrl = '/auth/verify?username=' + this.getUsername() + '&code=' + this.getCode()
    ApiClient.get(verificationUrl).then(this.props.verifyLoaded)
  }

  getUrlSearchParams() {
    return new URLSearchParams(window.location.search)
  }

  getUsername() {
    return this.getUrlSearchParams().get('username')
  }

  getCode() {
    return this.getUrlSearchParams().get('code')
  }

  displayContent() {
    if (this.props.verifying) {
      return <Loader/>
    } else {
      if (this.props.verifyResponse.message) {
        return (
          <>
            <p className='message'>{this.props.verifyResponse.message}</p>
            <Login/>
          </>
        )

      } else {
        return <p className='error'>{this.props.verifyResponse.error}</p>
      }
    }
  }

  render() {
    return (
      <section>
        <div id='verify-container'>
          <h2>Account Verification</h2>
          {this.displayContent()}
        </div>
      </section>
    )
  }
}

const verifyLoading = () => {
  return {
    type: 'VERIFY_LOADING'
  }
}

const verifyLoaded = (verifyResponse) => {
  return {
    type: 'VERIFY_LOADED',
    value: verifyResponse
  }
}

const mapStateToProps = state => {
  return {
    verifying: get(state, 'verify.loading', false),
    verifyResponse: get(state, 'verify.value', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    verifyLoading: bindActionCreators(verifyLoading, dispatch),
    verifyLoaded: bindActionCreators(verifyLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Verify)