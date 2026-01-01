import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import get from 'lodash/get'
import {connect} from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'
import AuthHelper from 'admin/Auth/AuthHelper'
import './header.scss'
import {APP_BASE_PATH} from 'admin/AdminApp'

class Header extends Component {
  displayMenu() {
    if (this.props.isLoggedIn) {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui">Home</Link>
          </nav>
          <nav>
            <Link to="/ui/decks">Decks</Link>
          </nav>
          <nav>
            <Link to="/ui/play">Play</Link>
            <ul className="dropdown">
              <li><Link to="/ui/play">Play</Link></li>
              <li><Link to="/ui/play/game-history">Game History</Link></li>
              <li><Link to="/ui/play/score-board">Score Board</Link></li>
            </ul>
          </nav>
          <nav>
            <Link to="/ui/profile">{this.props.profile.username}</Link>
            <ul className="dropdown">
              <li><Link to="/ui/profile">Profile</Link></li>
              { this.props.isNonGuest && <li><Link to="/ui/auth/change-password">Change Password</Link></li> }
              <li><Logout/></li>
            </ul>
          </nav>
        </div>
      )
    } else {
      return (
        <div id='menu-bar'>
          <nav>
            <Link to="/ui">Home</Link>
          </nav>
          <nav>
            <Link to="/ui/auth/login">Login</Link>
          </nav>
          <nav>
            <Link to="/ui/auth/register">Register</Link>
          </nav>
        </div>
      )
    }
  }

  render() {
    return (
      <header>
        <div id='logo'>
          <img src={APP_BASE_PATH + '/img/matag.png'} alt='matag-logo'/>
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
    isNonGuest: AuthHelper.isNonGuest(state),
    profile: get(state, 'session.profile', {}),
    config: get(state, 'config', {})
  }
}

const mapDispatchToProps = () => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Header)