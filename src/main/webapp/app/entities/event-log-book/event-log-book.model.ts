import dayjs from 'dayjs/esm';
import { IEventLog } from 'app/entities/event-log/event-log.model';
import { IUser } from 'app/entities/user/user.model';

export interface IEventLogBook {
  id?: number;
  uuid?: string | null;
  createdDate?: dayjs.Dayjs | null;
  updatedDate?: dayjs.Dayjs | null;
  name?: string;
  description?: string;
  archieved?: boolean | null;
  eventLogs?: IEventLog[] | null;
  user?: IUser | null;
}

export class EventLogBook implements IEventLogBook {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public updatedDate?: dayjs.Dayjs | null,
    public name?: string,
    public description?: string,
    public archieved?: boolean | null,
    public eventLogs?: IEventLog[] | null,
    public user?: IUser | null
  ) {
    this.archieved = this.archieved ?? false;
  }
}

export function getEventLogBookIdentifier(eventLogBook: IEventLogBook): number | undefined {
  return eventLogBook.id;
}
