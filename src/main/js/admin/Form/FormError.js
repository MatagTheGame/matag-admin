import React from 'react'

export default function FormError({error}) {
  if (error) {
    return (
      <div className='grid grid-label-value'>
        <div/>
        <div className='error'>{error}</div>
      </div>
    )
  }
}