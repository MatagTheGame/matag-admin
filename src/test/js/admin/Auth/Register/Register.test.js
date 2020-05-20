import 'babel-polyfill'
import ApiClientStub from '../../utils/ApiClientStub'
import TestUtils from '../../utils/TestUtils'
import Browser from '../../../Browser'

describe('Register', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
    TestUtils.userIsNotLoggedIn()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should register a user', async () => {
    // Given
    ApiClientStub.stubRegister({message: 'Registration Successful'})

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/register'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Register')
    browser.getRegisterSection().setEmail('MyUsername')
    browser.getRegisterSection().setUsername('MyUsername')
    browser.getRegisterSection().setPassword('MyPassword')
    browser.getRegisterSection().register()

    // Then
    await browser.getRegisterSection().waitForLoaderToDisappear()
    expect(browser.getRegisterSection().getMessage()).toBe('Registration Successful')
  })

  test('Should show an error when registration fails', async () => {
    // Given
    ApiClientStub.stubRegister({error: 'Registration Error'})

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/auth/register'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Register')
    browser.getRegisterSection().setEmail('MyUsername')
    browser.getRegisterSection().setUsername('MyUsername')
    browser.getRegisterSection().setPassword('MyPassword')
    browser.getRegisterSection().register()

    // Then
    await browser.getRegisterSection().waitForLoaderToDisappear()
    expect(browser.getRegisterSection().getError()).toBe('Registration Error')
  })
})
