import React from 'react'
import { Link } from 'react-router-dom'
import { useSelector } from 'react-redux'
import Logout from 'admin/Auth/Logout/Logout'
import AuthHelper from 'admin/Auth/AuthHelper'
import { APP_BASE_PATH } from 'admin/AdminApp'
import './header.scss'

export default function Header() {
  const { isLoggedIn, isNonGuest, profile, config } = useSelector(state => ({
    isLoggedIn: AuthHelper.isLoggedIn(state),
    isNonGuest: AuthHelper.isNonGuest(state),
    profile: state.session?.profile || {},
    config: state.config || {}
  }))

  return (
    <header>
      <div id='logo'>
        <img src={`${APP_BASE_PATH}/img/matag.png`} alt='matag-logo' />
        <h1>{config.matagName}</h1>
      </div>

      <div id='menu-bar'>
        <nav>
          <Link to="/ui">Home</Link>
        </nav>

        {isLoggedIn ? (
          <>
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
              <Link to="/ui/profile">{profile.username}</Link>
              <ul className="dropdown">
                <li><Link to="/ui/profile">Profile</Link></li>
                {isNonGuest && (
                  <li><Link to="/ui/auth/change-password">Change Password</Link></li>
                )}
                <li><Logout /></li>
              </ul>
            </nav>
          </>
        ) : (
          <>
            <nav>
              <Link to="/ui/auth/login">Login</Link>
            </nav>
            <nav>
              <Link to="/ui/auth/register">Register</Link>
            </nav>
          </>
        )}
      </div>
    </header>
  )
}
