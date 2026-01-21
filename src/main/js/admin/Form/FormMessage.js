import React from 'react'

export default function FormMessage({message}) {
  if (message) {
    return (
      <div className='grid grid-label-value'>
        <div/>
        <div className='message'>{message}</div>
      </div>
    )
  }
}