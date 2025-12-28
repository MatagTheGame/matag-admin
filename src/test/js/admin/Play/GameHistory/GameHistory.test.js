import 'babel-polyfill'
import ApiClientStub from '../../utils/ApiClientStub'
import TestUtils from '../../utils/TestUtils'
import Browser from '../../../Browser'

describe('GameHistory', () => {
  beforeEach(() => {
    TestUtils.defaultConfigAndStats()
    TestUtils.loginUser()
  })

  afterEach(() => {
    ApiClientStub.resetStubs()
  })

  test('Should see game history', async () => {
    // Given
    ApiClientStub.stubGameHistory({
      'gamesHistory': [{
        'gameId': 1,
        'startedTime': '2020-05-16T16:00:00.000000',
        'finishedTime': '2020-05-16T16:00:00.000000',
        'type': 'UNLIMITED',
        'result': 'LOST',
        'player1Name': 'Guest',
        'player1Options': '{"randomColors":["WHITE","BLACK"]}',
        'player2Name': 'Antonio85',
        'player2Options': '{"randomColors":["RED","GREEN"]}'
      }, {
        'gameId': 2,
        'startedTime': '2020-05-16T17:00:00.000000',
        'finishedTime': '2020-05-16T17:00:00.000000',
        'type': 'UNLIMITED',
        'result': 'WIN',
        'player1Name': 'Guest',
        'player1Options': '{"randomColors":["WHITE","BLUE"]}',
        'player2Name': 'Antonio85',
        'player2Options': '{"randomColors":["BLACK","GREEN"]}'
      }]
    })

    // When
    const browser = new Browser(TestUtils.renderAdminApp('/play/game-history'))
    await browser.waitUntilLoaded()

    // And
    await browser.waitForTitleToBe('Game History')

    // Then
    expect(browser.getGameHistorySection().getGameHistoryTableRows()).toEqual([
      ['5/16/2020, 4:00:00 PM', 'Guest', 'Antonio85', 'LOST', 'UNLIMITED'],
      ['5/16/2020, 5:00:00 PM', 'Guest', 'Antonio85', 'WIN', 'UNLIMITED'],
    ])
  })
})
