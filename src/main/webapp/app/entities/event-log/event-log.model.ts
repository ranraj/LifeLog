import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ITags } from 'app/entities/tags/tags.model';
import { IEventLogBook } from 'app/entities/event-log-book/event-log-book.model';
import { IEventLogType } from 'app/entities/event-log-type/event-log-type.model';

export interface IEventLog {
  id?: number;
  uuid?: string | null;
  name?: string | null;
  detail?: string;
  createdDate?: dayjs.Dayjs | null;
  updatedDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  tags?: ITags[] | null;
  eventLogBook?: IEventLogBook | null;
  eventLogTypes?: IEventLogType[] | null;
}

export class EventLog implements IEventLog {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public name?: string | null,
    public detail?: string,
    public createdDate?: dayjs.Dayjs | null,
    public updatedDate?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public tags?: ITags[] | null,
    public eventLogBook?: IEventLogBook | null,
    public eventLogTypes?: IEventLogType[] | null
  ) {}
}

export function getEventLogIdentifier(eventLog: IEventLog): number | undefined {
  return eventLog.id;
}
