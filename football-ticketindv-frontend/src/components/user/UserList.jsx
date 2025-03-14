import React from 'react';
import UserItem from './UserItem';

const UserList = ({ users, onDelete }) => {
  return (
    <div>
      <div className="list-group gap-3">
        {users.map((user) => (
          <UserItem key={user.id} user={user} onDelete={onDelete} />
        ))}
      </div>
    </div>
  );
};

export default UserList;