import TestUtils from './TestUtils'
import {match} from '../../../../../node/yarn/dist/lib/cli'

export default class ApiClientStub {
  static activeStubs = [];

  static stubConfig(config = TestUtils.DEFAULT_CONFIG) {
    this.addStub('GET', '/config', config);
  }

  static stubStats(stats = TestUtils.DEFAULT_STATS) {
    this.addStub('GET', '/stats', stats);
  }

  static stubProfile(profile = TestUtils.DEFAULT_PROFILE) {
    this.addStub('GET', '/profile', profile);
  }

  static stubActiveGame() {
    this.addStub('GET', '/game', {});
  }

  static stubLogin(loginResponse) {
    this.addStub('POST', '/auth/login', loginResponse);
  }

  static stubRegister(registrationResponse) {
    this.addStub('POST', '/auth/register', registrationResponse);
  }

  static stubChangePassword(changePasswordResponse) {
    this.addStub('POST', '/auth/change-password', changePasswordResponse);
  }

  static stubGameHistory(gameHistoryResponse) {
    this.addStub('GET', '/game/history', gameHistoryResponse);
  }

  static stubGameScores(gameScoresResponse) {
    this.addStub('GET', '/game/scores', gameScoresResponse);
  }

  static addStub(method, url, response) {
    this.activeStubs.unshift({
      method: method.toUpperCase(),
      url: '/matag/admin' + url,
      response
    });

    fetch.mockResponse((req) => {
      const match = this.activeStubs.find(stub =>
          req.url.includes(stub.url) && req.method === stub.method
      );

      if (match) {
        return Promise.resolve({
          body: JSON.stringify(match.response),
          status: 200,
          headers: { 'Content-Type': 'application/json' }
        });
      }

      // Fallback for unmocked calls so the test doesn't hang
      console.warn(`[ApiClientStub] No match for ${req.method} ${req.url}`);
      return Promise.resolve({ status: 404, body: 'Not Found' });
    });
  }

  static resetStubs() {
    this.activeStubs = [];
    fetch.resetMocks();
  }
}