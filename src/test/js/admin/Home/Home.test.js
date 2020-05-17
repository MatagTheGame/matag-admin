import 'babel-polyfill'
import TestUtils from '../../TestUtils'
import HomePage from './HomePage'

describe('Home', () => {
  beforeEach(() => {
    TestUtils.mockConfigAndStats()
  });

  afterEach(() => {
    TestUtils.resetMocks()
  });

  test('Should load homepage for non logged in user', async () => {
    // Given
    TestUtils.guestUser()

    // When
    const homePage = new HomePage(TestUtils.renderAdminApp())
    await homePage.waitUntilLoaded()

    // Then
    homePage.getHeader().expectTitleToBeMatagTheGame()
    homePage.getHeader().expectMenuItems(['Home', 'Login', 'Register'])
  })

  test('Should load homepage for logged in user', async () => {
    // Given
    TestUtils.mockActiveGame()
    TestUtils.userIsLoggedIn()

    // When
    const homePage = new HomePage(TestUtils.renderAdminApp())
    await homePage.waitUntilLoaded()

    // Then
    homePage.getHeader().expectTitleToBeMatagTheGame()
    homePage.getHeader().expectMenuItems(['Home', 'Decks', 'Play', 'User1'])
  })
})
