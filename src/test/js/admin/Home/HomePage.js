import {getNodeText} from '@testing-library/react'
import AbstractPage from '../../AbstractPage'
import IntroSection from './Intro/IntroSection'
import StatsSection from './Stats/StatsSection'

export default class HomePage extends AbstractPage {
  constructor(app) {
    super(app)
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
}
