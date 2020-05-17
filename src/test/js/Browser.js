import {getNodeText, waitFor} from '@testing-library/react'
import HeaderSection from './admin/Header/HeaderSection'
import IntroSection from './admin/Home/Intro/IntroSection'
import StatsSection from './admin/Home/Stats/StatsSection'
import LoginSection from './admin/Auth/LoginSection'
import PlaySection from './admin/Play/PlaySection'

export default class Browser {
  constructor(app) {
    this.app = app
  }

  async waitUntilLoaded() {
    await waitFor(() => this.app.container.querySelector('.page'))
  }

  getHeader() {
    return new HeaderSection(this.app.container.querySelector('header'))
  }

  expectTitleToBeHome() {
    expect(getNodeText(this.app.container.querySelector('h2'))).toBe('Home')
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

  getPlaySection() {
    return new PlaySection(this.app.container.querySelector('#play'))
  }
}