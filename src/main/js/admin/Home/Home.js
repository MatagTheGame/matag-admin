import React from 'react'
import { useSelector } from 'react-redux'
import AuthHelper from 'admin/Auth/AuthHelper'
import Login from 'admin/Auth/Login/Login'
import Play from 'admin/Play/Play'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'
import './home.scss'

export default function Home() {
  const { isLoggedIn, config } = useSelector(state => ({
    isLoggedIn: AuthHelper.isLoggedIn(state),
    config: state.config || {}
  }))

  return (
    <section id='home'>
      <h2>Home</h2>
      <Intro config={config} />
      <Stats />
      {isLoggedIn ? <Play /> : <Login />}
    </section>
  )
}
