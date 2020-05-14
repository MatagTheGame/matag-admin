import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import get from 'lodash/get'
import {connect} from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'
import AuthHelper from '../Auth/AuthHelper'
import './header.scss'

class Header extends Component {
  displayMenu() {
    if (this.props.isLoggedIn) {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui/admin">Home</Link>
          </nav>
          <nav>
            <Link to="/ui/admin/decks">Decks</Link>
          </nav>
          <nav>
            <Link to="/ui/admin/play">Play</Link>
          </nav>
          <nav>
            <Logout/>
          </nav>
          <nav className='welcome'>
            <span>Welcome {this.props.profile.username}</span>
          </nav>
        </div>
      )
    } else {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui/admin">Home</Link>
          </nav>
          <nav>
            <Link to="/ui/admin/auth/login">Login</Link>
          </nav>
          <nav>
            <Link to="/ui/admin/auth/register">Register</Link>
          </nav>
        </div>
      )
    }
  }

  render() {
    return (
      <header>
        <div id='logo'>
          <img src='/img/matag.png' alt='matag-logo'/>
          <h1>{this.props.config.matagName}</h1>
        </div>
        {this.displayMenu()}
      </header>
    )
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state),
    profile: get(state, 'session.profile', {}),
    config: get(state, 'config', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Header)