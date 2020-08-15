import fetchMock from 'fetch-mock'
import TestUtils from './TestUtils'

export default class ApiClientStub {
  static stubConfig(config = TestUtils.DEFAULT_CONFIG) {
    fetchMock.get('/config', config)
  }

  static stubStats(stats = TestUtils.DEFAULT_STATS) {
    fetchMock.get('/stats', stats)
  }

  static stubProfile(profile = TestUtils.DEFAULT_PROFILE) {
    fetchMock.get('/profile', profile)
  }

  static stubActiveGame() {
    fetchMock.get('/game', {})
  }

  static stubLogin(loginResponse) {
    fetchMock.post('/auth/login', loginResponse)
  }

  static stubRegister(registrationResponse) {
    fetchMock.post('/auth/register', registrationResponse)
  }

  static stubChangePassword(changePasswordResponse) {
    fetchMock.post('/auth/change-password', changePasswordResponse)
  }

  static stubGameHistory(gameHistoryResponse) {
    fetchMock.get('/game/history', gameHistoryResponse)
  }

  static stubGameScores(gameScoresResponse) {
    fetchMock.get('/game/scores', gameScoresResponse)
  }

  static resetStubs() {
    fetchMock.reset()
  }
}