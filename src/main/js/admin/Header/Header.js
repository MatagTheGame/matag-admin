import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import get from 'lodash/get'
import {connect} from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'
import AuthHelper from 'admin/Auth/AuthHelper'
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
            <ul className="dropdown">
              <li><Link to="/ui/admin/play/game-history">Game History</Link></li>
              <li><Link to="/ui/admin/play/score-board">Score Board</Link></li>
            </ul>
          </nav>
          <nav>
            <Link to="/ui/admin/profile">{this.props.profile.username}</Link>
            <ul className="dropdown">
              <li><Link to="/ui/admin/profile">Profile</Link></li>
              <li><Logout/></li>
            </ul>
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

const mapDispatchToProps = () => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Header)