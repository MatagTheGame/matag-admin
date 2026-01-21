import React from 'react'
import './loader.scss'
import {APP_BASE_PATH} from 'admin/AdminApp'

export default function Loader({center, fullscreen}) {
  const getClassNames = () => {
    if (center) {
      return 'center'
    }

    if (fullscreen) {
      return 'fullscreen-loader'
    }
  }

  return (
    <div className={getClassNames()}>
      <img className='loader' alt='loader' src={APP_BASE_PATH + '/img/loader.gif'} />
    </div>
  )
}
