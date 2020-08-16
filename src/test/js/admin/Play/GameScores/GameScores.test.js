import 'babel-polyfill'
import ApiClientStub from '../../utils/ApiClientStub'
import TestUtils from '../../utils/TestUtils'
import Browser from '../../../Browser'

describe('GameScores', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
    TestUtils.userIsLoggedIn()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should see score board', async () => {
    // Given
    ApiClientStub.stubGameScores({
      'scores': [{
        'rank': 1,
        'player': 'Foo',
        'elo' : 1600,
        'wins' : 4,
        'draws' : 1,
        'losses' : 2
      }, {
        'rank': 2,
        'player': 'Bar',
        'elo' : 1200,
        'wins' : 2,
        'draws' : 1,
        'losses' : 4
      }]
    })

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/play/score-board'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Score Board')

    // Then
    expect(browser.getGameScoresSection().getGameScoresTableRows()).toEqual([
      ['1', 'Foo', '1600', '4', '1', '2'],
      ['2', 'Bar', '1200', '2', '1', '4']
    ])
  })
})
