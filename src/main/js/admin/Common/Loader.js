import React, {Component} from 'react'
import './loader.scss'
import {APP_BASE_PATH} from 'admin/utils/ApiClient'

export default class Loader extends Component {
  constructor(props) {
    super(props)
  }

  getClassNames() {
    if (this.props.center) {
      return 'center'
    }

    if (this.props.fullscreen) {
      return 'fullscreen-loader'
    }
  }

  render() {
    return (
      <div className={this.getClassNames()}>
        <img className='loader' alt='loader' src={APP_BASE_PATH + '/img/loader.gif'} />
      </div>
    )
  }
}
