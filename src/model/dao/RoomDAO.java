package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Stanza;

public interface RoomDAO {

    Stanza insert(Stanza stanza) throws DuplicatedObjectException;

    void update(Stanza stanza);

    void delete(Stanza stanza);

    Stanza[] findAllRooms();

    Stanza findRoom(int roomId);

}
