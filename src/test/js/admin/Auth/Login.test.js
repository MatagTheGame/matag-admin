import 'babel-polyfill'
import ApiClientStub from '../utils/ApiClientStub'
import TestUtils from '../utils/TestUtils'
import Browser from '../../Browser'

describe('Login', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should login a user', async () => {
    // Given
    TestUtils.userIsNotLoggedIn()
    ApiClientStub.stubLogin({token: 'token', profile: TestUtils.DEFAULT_PROFILE})
    ApiClientStub.stubActiveGame()

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/login'))
    await browser.waitUntilLoaded()
    browser.getHeader().expectMenuItems(['Home', 'Login', 'Register'])

    // Then
    await browser.waitForTitleToBe('Login')
    browser.getLoginSection().setUsername('MyUsername')
    browser.getLoginSection().setPassword('MyPassword')
    browser.getLoginSection().login()

    await browser.waitForTitleToBe('Home')
    browser.getHeader().expectMenuItems(['Home', 'Decks', 'Play', 'User1'])
  })

  test('Should display errors when login fails', async () => {
    // Given
    TestUtils.userIsNotLoggedIn()
    ApiClientStub.stubLogin({error: 'Wrong password'})

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/login'))
    await browser.waitUntilLoaded()
    browser.getHeader().expectMenuItems(['Home', 'Login', 'Register'])

    // Then
    await browser.waitForTitleToBe('Login')
    browser.getLoginSection().setUsername('MyUsername')
    browser.getLoginSection().setPassword('WrongPassword')
    browser.getLoginSection().login()

    await browser.getLoginSection().waitForLoaderToDisappear()
    expect(browser.getLoginSection().getError()).toBe('Wrong password')
  })
})
