import React from 'react'
import { useSelector } from 'react-redux'

export default function Profile() {
  const profile = useSelector(state => state.session?.profile || {})

  return (
    <section id='profile'>
      <h2>Profile</h2>
      <dl>
        <dt>Username: </dt>
        <dd>{profile.username}</dd>
        <dt>Type: </dt>
        <dd>{profile.type}</dd>
      </dl>
    </section>
  )
}