import ApiClientStub from '../utils/ApiClientStub'
import TestUtils from '../utils/TestUtils'
import Browser from '../../Browser'

describe('Profile', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test.skip('Should display profile', async () => {
    // Given
    ApiClientStub.stubActiveGame()
    TestUtils.loginUser()

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/profile'))
    await browser.waitUntilLoaded()

    // Then
    await browser.waitForTitleToBe('Profile')
    browser.getProfileSection('profile').validateProfile(TestUtils.DEFAULT_PROFILE)
  })
})
