import {getNodeText, waitFor} from '@testing-library/react'
import HeaderSection from './admin/Header/HeaderSection'
import IntroSection from './admin/Home/Intro/IntroSection'
import StatsSection from './admin/Home/Stats/StatsSection'
import LoginSection from './admin/Auth/Login/LoginSection'
import PlaySection from './admin/Play/PlaySection'
import ProfileSection from './admin/Profile/ProfileSection'
import RegisterSection from './admin/Auth/Register/RegisterSection'

export default class Browser {
  constructor(app) {
    this.app = app
  }

  async waitUntilLoaded() {
    await waitFor(() => this.app.container.querySelector('.page'))
  }

  async waitForTitleToBe(title) {
    await waitFor(() => getNodeText(this.app.container.querySelector('h2')) === title)
  }

  getHeader() {
    return new HeaderSection(this.app.container.querySelector('header'))
  }

  getIntroSection() {
    return new IntroSection(this.app.container.querySelector('#intro'))
  }

  getStatsSection() {
    return new StatsSection(this.app.container.querySelector('#stats'))
  }

  getLoginSection() {
    return new LoginSection(this.app.container.querySelector('#login'))
  }

  getRegisterSection() {
    return new RegisterSection(this.app.container.querySelector('#register'))
  }

  getPlaySection() {
    return new PlaySection(this.app.container.querySelector('#play'))
  }

  getProfileSection() {
    return new ProfileSection(this.app.container.querySelector('#profile'))
  }
}