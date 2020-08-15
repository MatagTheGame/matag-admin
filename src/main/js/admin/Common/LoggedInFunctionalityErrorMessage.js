import React, {Component} from 'react'
import './loader.scss'

export default class LoggedInFunctionalityErrorMessage extends Component {
  render() {
    return (
      <div>
        This functionality is available only to logged in users.
      </div>
    )
  }
}
